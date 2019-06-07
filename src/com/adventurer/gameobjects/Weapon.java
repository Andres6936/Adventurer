package com.adventurer.gameobjects;

import com.adventurer.data.ItemBonus;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.enumerations.WeaponType;

public class Weapon extends Equippable
{

    private WeaponType weaponType;
    private WeaponSlot weaponSlot;

    public Weapon( Tile tile, SpriteType spritetype, String name, String description,
                   int value, ItemRarity itemRarity, ItemBonus bonus,
                   WeaponType weaponType, WeaponSlot weaponSlot )
    {
        super( tile, spritetype, name, description, value, itemRarity, bonus );

        this.weaponType = weaponType;
        this.weaponSlot = weaponSlot;

    }

    public WeaponType getWeaponType( ) { return weaponType; }

    public WeaponSlot getWeaponSlot( ) { return weaponSlot; }
}
