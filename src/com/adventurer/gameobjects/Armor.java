package com.adventurer.gameobjects;

import com.adventurer.data.ItemBonus;
import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;

public class Armor extends Equippable
{

    private ArmorSlot slot;

    public Armor( Tile tile, SpriteType spritetype, String name, String description,
                  int value, ItemRarity itemRarity, ArmorSlot armorSlot, ItemBonus bonus )
    {
        super( tile, spritetype, name, description, value, itemRarity, bonus );

        this.slot = armorSlot;
    }

    public ArmorSlot getSlot( ) { return this.slot; }
}
