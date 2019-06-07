package com.adventurer.main;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.Enemy;
import com.adventurer.gameobjects.Tile;

public class EnemyLoSManager
{

    // http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
    private List< Tile > calculateLine( int x, int y, int x2, int y2 )
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
                        tile.GetActor( ) != null && tile.GetActor( ) instanceof Enemy == false
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

    public Tile[] GetVisibleTiles( Coordinate position )
    {

        // list of tiles that are visible
        List< Tile > foundTiles = new ArrayList< Tile >( );

        World world = World.instance.GetWorld( );

        List< Tile > tiles_ = new ArrayList< Tile >( world.GetTiles( ) );

        for ( Tile tile : tiles_ )
        {

            int targetx = tile.GetTilePosition( ).getX( );
            int targety = tile.GetTilePosition( ).getY( );

            for ( Tile t : calculateLine( position.getX( ), position.getY( ), targetx, targety ) )
            {
                if ( foundTiles.contains( t ) == false ) foundTiles.add( t );
            }
        }
        Tile[] allTiles = new Tile[ foundTiles.size( ) ];
        allTiles = foundTiles.toArray( allTiles );
        return allTiles;
    }
}