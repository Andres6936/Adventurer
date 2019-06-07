package com.adventurer.gameobjects;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.ShrineType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.DamageHandler;
import com.adventurer.main.SpriteCreator;

public class Shrine extends Tile
{

    private boolean used = false;
    private int amount = 0;
    private ShrineType shrineType;

    public Shrine( Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type, ShrineType shrineType, int amount )
    {
        super( worldPos, tilePos, spritetype, type );

        this.amount = amount;
        this.shrineType = shrineType;
    }

    public void activate( )
    {

        if ( used == false )
        {

            if ( shrineType == ShrineType.healing )
            {

                if ( this.actor != null )
                {
                    DamageHandler.ActorHeal( this.actor, this.amount );
                }

            }
            else
            {
                System.out.print( "NOT YET IMPLEMENTED SHRINETYPE." );
                return;
            }

            // change sprite.
            this.SetSprite( SpriteCreator.instance.CreateSprite( SpriteType.UsedShrine ) );

            // change flag
            used = true;
        }
    }

    public String toString( )
    {
        if ( used == false )
        {
            return this.getShrineType( ) + " " + this.GetTileType( ).toString( ) + " (" + this.amount + ")";
        }
        else
        {
            return this.getShrineType( ) + " " + this.GetTileType( ).toString( ) + " (used)";
        }
    }

    public int getAmount( ) { return amount; }

    public void setAmount( int amount ) { this.amount = amount; }

    public ShrineType getShrineType( ) {return shrineType; }

    public void setShrineType( ShrineType shrineType ) { this.shrineType = shrineType; }
}
