package com.adventurer.gameobjects;

import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;

public abstract class Usable extends Item
{

    public Usable( Tile tile, SpriteType spritetype, String name, String description, int value, ItemRarity itemRarity )
    {
        super( tile, spritetype, name, description, value, itemRarity );
    }

    public abstract void use( );

}
