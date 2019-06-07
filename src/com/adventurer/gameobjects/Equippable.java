package com.adventurer.gameobjects;

import com.adventurer.data.ItemBonus;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;

public abstract class Equippable extends Item
{

    protected ItemBonus bonus;

    // only res or dmg
    public Equippable( Tile tile, SpriteType spritetype, String name, String description,
                       int value, ItemRarity itemRarity, ItemBonus bonus )
    {
        super( tile, spritetype, name, description, value, itemRarity );

        this.bonus = bonus;
    }

    public ItemBonus getBonuses( ) { return this.bonus; }
}
