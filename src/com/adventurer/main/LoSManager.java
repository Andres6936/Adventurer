package com.adventurer.main;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.*;
import com.adventurer.utilities.Util;

public class LoSManager
{

    private List< Tile > cachedLos = new ArrayList< Tile >( );

    public LoSManager( ) {}

    // http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
    public List< Tile > calculateLine( int x, int y, int x2, int y2 )
    {

        List< Tile > tiles = new ArrayList< Tile >( );

        int w = x2 - x;
        int h = y2 - y;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;

        if ( w < 0 ) { dx1 = - 1; }
        else if ( w > 0 ) dx1 = 1;
        if ( h < 0 ) { dy1 = - 1; }
        else if ( h > 0 ) dy1 = 1;
        if ( w < 0 ) { dx2 = - 1; }
        else if ( w > 0 ) dx2 = 1;

        int longest = Math.abs( w );
        int shortest = Math.abs( h );

        if ( longest > shortest == false )
        {
            longest = Math.abs( h );
            shortest = Math.abs( w );
            if ( h < 0 ) { dy2 = - 1; }
            else if ( h > 0 ) dy2 = 1;
            dx2 = 0;
        }

        int numerator = longest >> 1;

        for ( int i = 0; i <= longest; i++ )
        {

            Tile tile = World.instance.GetTileAtPosition( x, y );

            if ( tile != null )
            {

                tiles.add( tile );

                if ( tile.GetTileType( ) == TileType.OuterWall || tile.GetTileType( ) == TileType.Wall ||
                        tile.GetTileType( ) == TileType.DestructibleTile || tile.GetTileType( ) == TileType.Door ||
                        tile.GetTileType( ) == TileType.LockedDoor ||
                        tile.GetTileType( ) == TileType.Portal ||
                        tile.GetActor( ) != null && tile.GetActor( ) instanceof Player == false
                ) { break; }

            }

            numerator += shortest;

            if ( numerator < longest == false )
            {
                numerator -= longest;
                x += dx1;
                y += dy1;
            }
            else
            {
                x += dx2;
                y += dy2;
            }

        }

        return tiles;
    }

    public void CalculateLos( Coordinate position )
    {

        if ( World.instance == null ) return;
        World world = World.instance.GetWorld( );
        List< Tile > allTiles = world.GetTiles( );

        // draw cached FOV.
        // -> means we don't always update FOV.
        if ( Util.GetRandomInteger( ) < 50 )
        {
            hideAllTiles( allTiles );
            showVisibleTiles( cachedLos );
            return;
        }

        // 1. hide all tiles.
        hideAllTiles( allTiles );

        // list of tiles that are visible
        List< Tile > foundTiles = new ArrayList< Tile >( );

        // 2. calculate FOV
        // 	  -> using Bresenham's line algorithm
        // ------------------------
        try
        {

            List< Tile > tiles_ = new ArrayList< Tile >( allTiles );
            for ( Tile tile : tiles_ )
            {

                int targetx = tile.GetTilePosition( ).getX( );
                int targety = tile.GetTilePosition( ).getY( );

                for ( Tile t : calculateLine( position.getX( ), position.getY( ), targetx, targety ) )
                {
                    if ( foundTiles.contains( t ) == false ) foundTiles.add( t );
                }
            }

        }
        catch ( ConcurrentModificationException e )
        {
            e.printStackTrace( );
            System.exit( 1 );
        }

        // ------------------------
        // 3. show visible-flagged tiles
        showVisibleTiles( foundTiles );

        // 4. cache found tiles.
        cachedLos = foundTiles;
    }

    private void showVisibleTiles( List< Tile > foundTiles )
    {
        for ( Tile tile : foundTiles )
        {
            if ( tile.isDiscovered( ) == false ) tile.Discover( );
            tile.Show( );
        }
    }

    private void hideAllTiles( List< Tile > allTiles )
    {
        List< Tile > tiles_ = new ArrayList< Tile >( allTiles );
        for ( Tile tile : tiles_ ) { tile.Hide( ); }
    }
}
