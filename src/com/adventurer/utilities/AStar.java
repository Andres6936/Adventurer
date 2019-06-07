package com.adventurer.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adventurer.data.Node;
import com.adventurer.data.World;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.Tile;

// Implemented from Wikipedia's AStar article.
public class AStar
{

    public static List< Tile > CalculatePath( Tile start, Tile goal )
    {

        // reset nodes
        for ( Tile tile : World.instance.GetTiles( ) ) tile.getNode( ).reset( );

        Map< Tile, Tile > cameFrom = new HashMap< Tile, Tile >( ); // save path tile and it's child tile
        List< Tile > closedSet = new ArrayList< Tile >( );     //
        List< Tile > openSet = new ArrayList< Tile >( );     // visited

        openSet.add( start );
        start.getNode( ).setgScore( 0d );
        start.getNode( ).setfScore( heuristic_cost_estimate( start, goal ) );

        while ( openSet.isEmpty( ) == false )
        {

            Tile current = getNodeWithLowestFScore( openSet );
            if ( current == goal ) return reconstruct_path( cameFrom, current, goal );

            openSet.remove( current );
            closedSet.add( current );

            for ( Tile neighbor : World.instance.GetTilesInCardinalDirection( current ) )
            {

                if ( neighbor.GetTileType( ) == TileType.Wall ||
                        neighbor.GetTileType( ) == TileType.OuterWall ||
                        neighbor.GetTileType( ) == TileType.DestructibleTile ||
                        neighbor.GetTileType( ) == TileType.Door ||
                        neighbor.GetTileType( ) == TileType.LockedDoor ||
                        neighbor.GetActor( ) != null && neighbor.GetActor( ) == start.GetActor( ) ) { continue; }


                if ( closedSet.contains( neighbor ) ) continue;
                if ( openSet.contains( neighbor ) == false ) openSet.add( neighbor );

                double tentative_gScore = current.getNode( ).getgScore( ) + World.instance.distanceBetweenTiles( current, neighbor );
                if ( tentative_gScore >= neighbor.getNode( ).getgScore( ) ) continue;

                cameFrom.put( neighbor, current );
                neighbor.getNode( ).setgScore( tentative_gScore );
                neighbor.getNode( ).setfScore( tentative_gScore + heuristic_cost_estimate( neighbor, goal ) );
            }
        }
        return null;
    }

    private static List< Tile > reconstruct_path( Map< Tile, Tile > cameFrom, Tile current, Tile goal )
    {
        List< Tile > totalPath = new ArrayList< Tile >( );
        totalPath.add( goal );

        while ( cameFrom.containsKey( current ) )
        {
            current = cameFrom.get( current );
            totalPath.add( current );
        }

        // remove the tile the enemy is currently on.
        totalPath.remove( totalPath.size( ) - 1 );
        return totalPath;
    }

    private static double heuristic_cost_estimate( Tile neighbor, Tile goal )
    {
        // TODO: calculate traps etc.
        return 0d;
    }

    private static Tile getNodeWithLowestFScore( List< Tile > openSet )
    {
        double lowestScore = Double.POSITIVE_INFINITY;
        Tile chosenTile = null;

        for ( Tile tile : openSet )
        {
            Node node = tile.getNode( );
            if ( node.getfScore( ) < lowestScore )
            {
                lowestScore = node.getfScore( );
                chosenTile = tile;
            }
        }
        return chosenTile;
    }
}
