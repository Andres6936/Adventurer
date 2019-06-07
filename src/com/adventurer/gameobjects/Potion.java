package com.adventurer.gameobjects;

import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.enumerations.Effect;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.ActorManager;
import com.adventurer.main.DamageHandler;

public class Potion extends Usable
{

    private Map< Effect, Integer > effects;

    public Potion( Tile tile, SpriteType spritetype, String name, String description, int value, Map< Effect, Integer > effects )
    {
        super( tile, spritetype, name, description, value, ItemRarity.Generic );

        this.effects = effects;
    }

    public void use( )
    {

        Player player = ActorManager.GetPlayerInstance( );

        // remove item from inventory.
        player.getInventory( ).removeItemFromInventory( this );

        // do effects
        for ( Entry< Effect, Integer > e : effects.entrySet( ) )
        {

            Effect key = e.getKey( );
            int val = e.getValue( );

            switch ( key )
            {
                case GainHealth:
                    DamageHandler.ActorHeal( player, val ); break;
                case GainMana:
                    DamageHandler.actorRestoreMana( player, val ); break;
                default:
                    System.out.println( "EFFECT NOT IMPLEMENTED: " + key + " IN POTION.USE" ); break;
            }

        }

        // remove item
        this.Remove( );
    }

    public Map< Effect, Integer > getEffects( ) { return this.effects; }

}
