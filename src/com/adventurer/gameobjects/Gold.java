package com.adventurer.gameobjects;

import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;

public class Gold extends Item
{

    private int amount = 0;

    public Gold( Tile tile, SpriteType spritetype, int amount )
    {
        super( tile, spritetype, "Gold", "Currency", amount, ItemRarity.Generic );
        this.amount = amount;
    }

    public int getAmount( ) { return amount; }

    public void addAmount( int a ) { this.amount += a; }
}
