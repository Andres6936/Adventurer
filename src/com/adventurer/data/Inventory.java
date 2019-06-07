package com.adventurer.data;

import java.util.ArrayList;
import java.util.List;

import com.adventurer.enumerations.ItemNames;
import com.adventurer.enumerations.KeyType;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Key;
import com.adventurer.gameobjects.Projectile;
import com.adventurer.gameobjects.Tile;
import com.adventurer.main.Game;
import com.adventurer.main.ItemCreator;

public class Inventory
{

    private int maxInventorySpace;
    private List< Item > inventory = new ArrayList< Item >( );

    public Inventory( Actor actor )
    {

        this.maxInventorySpace = Game.DEFAULT_INVENTORY_MAX_SIZE;

        Tile tile = actor.getCurrentTile( );

        /*
         *  Player starting gear:
         *  1x dagger
         *  1x HP pot
         *  n. keys
         *
         */

        // populate inventory
        for ( int i = 0; i < Game.START_KEY_COUNT; i++ )
        { this.addToInventory( ItemCreator.createKey( tile, KeyType.Normal ) ); }
        this.addToInventory( ItemCreator.createWeapon( tile, ItemNames.Dagger, true ) );
        this.addToInventory( ItemCreator.createHealthPotion( tile, 15 ) );

    }

    // returns false if inventory is full
    public void addToInventory( Item item )
    {

        // stack gold.
        if ( item instanceof Gold )
        {
            Gold currentGold = this.getGold( );
            if ( currentGold != null )
            {
                currentGold.addAmount( ( ( Gold ) item ).getAmount( ) );
                return;
            }
        }

        if ( this.isFull( ) == false )
        {
            this.inventory.add( item );
        }
    }

    public Projectile getProjectile( )
    {
        Projectile proj = null;
        for ( Item item : inventory )
        {
            if ( item instanceof Projectile )
            {
                proj = ( Projectile ) item;
                break;
            }
        }
        return proj;
    }

    public Gold getGold( )
    {
        Gold gold = null;
        for ( Item item : inventory )
        {
            if ( item instanceof Gold )
            {
                gold = ( Gold ) item;
                break;
            }
        }
        return gold;
    }

    public Key getKey( KeyType keyType )
    {
        Key key = null;
        for ( Item item : inventory )
        {
            if ( item instanceof Key && ( ( Key ) item ).getKeyType( ) == keyType )
            {
                key = ( Key ) item;
                break;
            }
        }
        return key;
    }

    public void removeItemFromInventory( Item item ) { this.inventory.remove( item ); }

    public boolean isFull( )
    {
        if ( this.inventory.size( ) >= this.maxInventorySpace ) { return true; }
        else { return false; }
    }

    public int getMaxSize( ) { return this.maxInventorySpace; }

    public List< Item > getInventoryItems( ) { return this.inventory; }

    public Item getItemOnPosition( int index )
    {
        Item item = null;
        if ( index > this.maxInventorySpace || index < 0 || index > inventory.size( ) - 1 )
        {
            // fail return null
        }
        else
        {
            // index is between 0 - maxInvSpace
            item = inventory.get( index );
        }
        return item;
    }

}
