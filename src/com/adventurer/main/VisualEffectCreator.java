package com.adventurer.main;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.VisualEffect;
import com.adventurer.utilities.Util;
import com.adventurer.gameobjects.Tile;

public class VisualEffectCreator
{

    public static void CreateHitEffect( Tile tile )
    {
        new VisualEffect( tile.GetWorldPosition( ), tile.GetTilePosition( ), SpriteType.Hit01, 150 );
    }

    public static void CreateHealEffect( Tile tile, boolean multiple, SpriteType spriteType )
    {

        if ( multiple )
        {
            for ( int i = 0; i < Util.GetRandomInteger( 1, 5 ); i++ )
            {

                int maxOffset = Game.SPRITE_SIZE / 2;

                Coordinate offset = new Coordinate( Util.GetRandomInteger( - maxOffset, maxOffset ), Util.GetRandomInteger( - maxOffset, maxOffset ) );
                Coordinate pos = new Coordinate( tile.GetWorldPosition( ).getX( ) + offset.getX( ), tile.GetWorldPosition( ).getY( ) + offset.getY( ) );
                new VisualEffect( pos, tile.GetTilePosition( ), spriteType, 300 );
            }
        }
        else { new VisualEffect( tile.GetWorldPosition( ), tile.GetTilePosition( ), spriteType, 300 ); }

    }

}