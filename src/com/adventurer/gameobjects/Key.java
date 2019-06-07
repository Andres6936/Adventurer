package com.adventurer.gameobjects;

import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.KeyType;
import com.adventurer.enumerations.SpriteType;

public class Key extends Usable
{

    private KeyType keyType;

    public Key( Tile tile, SpriteType spritetype, String name, KeyType keyType, int value )
    {
        super( tile, spritetype, name, "Opens doors.", value, ItemRarity.Generic );
        this.keyType = keyType;
    }

    public KeyType getKeyType( ) { return keyType; }

    public void use( ) {}
}
