package com.adventurer.gameobjects;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.DamageHandler;

public class Bomb extends Usable
{

    private Map< DamageType, Integer > damage;

    private long liveTime = 0;
    private long liveTimer = 0;
    private boolean active = false;

    public Bomb( Tile tile, SpriteType spritetype, int liveTime,
                 Map< DamageType, Integer > damage, String name, String description, int value )
    {
        super( tile, spritetype, name, description, value, ItemRarity.Generic );

        this.damage = new LinkedHashMap< DamageType, Integer >( damage );
        this.liveTime = liveTime;
    }

    public void tick( )
    {

        if ( active == false ) return;

        if ( System.currentTimeMillis( ) > liveTimer )
        {

            // do damage to items near this position
            List< Tile > tiles = World.instance.GetTilesInCardinalDirection( this.GetTilePosition( ) );

            // add the current tile to the tiles list
            tiles.add( World.instance.GetTileAtPosition( this.GetTilePosition( ) ) );

            for ( Tile tile : tiles )
            {

                if ( tile instanceof Door )
                {

                    Door door = ( Door ) tile;

                    if ( tile.GetTileType( ) == TileType.LockedDoor || door.getDoorType( ) == DoorType.Diamond )
                    {
                        // dont do anything.
                    }
                    else { door.Open( ); }

                }
                else if ( tile.GetActor( ) != null ) DamageHandler.ActorTakeDamage( tile.GetActor( ), damage );

            }

            // destroy this object
            Remove( );
        }
    }

    public void use( )
    {
        //this.liveTimer = System.currentTimeMillis() + this.liveTime;
        //this.active = true;
    }

    public Map< DamageType, Integer > getDamage( ) { return damage; }

    public void setDamage( Map< DamageType, Integer > damage ) { this.damage = damage; }
}
