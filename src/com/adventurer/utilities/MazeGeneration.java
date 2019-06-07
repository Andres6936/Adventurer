package com.adventurer.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.Tile;

public class MazeGeneration
{

    // TODO: don't allow diagonal only corners
    // -----> because player can see diagonally.

    // uses recursive backtracking algorithm, based on Jamis Buck's work.
    // http://www.jamisbuck.org/presentations/rubyconf2011/index.html#recursive-backtracker-demo
    public static List< Tile > generateMaze( List< Tile > tiles )
    {

        List< Tile > tiles_ = new ArrayList< Tile >( tiles );  // contains all tiles
        Stack< Tile > visited = new Stack< Tile >( );           // visited tiles
        List< Tile > concretePath = new ArrayList< Tile >( );       // floor tiles
        List< Tile > concreteWalls = new ArrayList< Tile >( );       // wall tiles

        // randomize starting tile
        Tile current = tiles_.get( Util.GetRandomInteger( 0, tiles_.size( ) ) );
        visited.push( current );

        Tile cameFrom = null, neighbor = null;
        boolean backtracking = false;
        int c = 0; // TODO: this is ugly and has side effects.

        // ----------------------------------- START MAZE GEN
        while ( visited.isEmpty( ) == false )
        {

            current = visited.pop( );
            concretePath.add( current );

            do
            { // random walker

                if ( backtracking )
                {

                    neighbor = Util.getRandomNeighborWall( current, tiles_, concreteWalls );

                    if ( neighbor != null )
                    {

                        // our neighbor candidate should have error neighbors
                        // --> it should not be in walls or floors
                        for ( Tile t : Util.getNeighboringTiles( neighbor, tiles_ ) )
                        {

                            if (

                                    concretePath.contains( t ) == false &&
                                            concreteWalls.contains( t ) == false &&
                                            visited.contains( t ) == false

                            )
                            {

                                concreteWalls.remove( neighbor );

                                visited.push( neighbor );
                                cameFrom = current;
                                current = neighbor;
                                c = 0;

                                backtracking = false;
                                break;
                            }

                        }

                    }
                    else { continue; }
                }
                else
                {

                    neighbor = Util.getRandomNeighboringTile( current, tiles_ );

                    if (

                            ( neighbor != null &&
                                    visited.contains( neighbor ) == false &&
                                    concretePath.contains( neighbor ) == false &&
                                    concreteWalls.contains( neighbor ) == false )

                    )
                    {

                        // If we accidentally chose the same tile where we came from,
                        // then just continue and don't count this as a failure.
                        if ( neighbor.equals( cameFrom ) ) continue;

                        // other neighbors are turned into walls.
                        for ( Tile t : Util.getNeighboringTiles( current, tiles_ ) )
                        {
                            if (
                                    t != null &&
                                            t.equals( neighbor ) == false &&
                                            visited.contains( t ) == false &&
                                            concretePath.contains( t ) == false &&
                                            concreteWalls.contains( t ) == false

                            ) { concreteWalls.add( t ); }
                        }

                        visited.push( neighbor );
                        cameFrom = current; // cache the previous tile
                        current = neighbor;
                        c = 0;

                    }
                    else { c++; }
                }
            } while ( c < 20 ); // TODO: This is bad.

            // random walker got stuck.
            // --> backtrack.
            backtracking = true;
        }

        // ------------------------------------ END OF MAZE GEN

        // draw floor
        for ( Tile t : concretePath )
        {
            tiles_.remove( t );
            Tile newt = Util.replaceTile( t, TileType.Floor, SpriteType.Floor01 );
            tiles_.add( newt );
        }

        // draw walls
        for ( Tile t : concreteWalls )
        {
            tiles_.remove( t );
            Tile newt = Util.replaceTile( t, TileType.Wall, SpriteType.Wall01 );
            tiles_.add( newt );
        }
        return tiles_;
    }
}
