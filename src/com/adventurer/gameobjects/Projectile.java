package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.*;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Projectile extends Usable
{

    private Direction direction;
    private boolean alive = true;
    private boolean tileDiscovered = true;

    private boolean shot = false;

    private Map< DamageType, Integer > damage;
    private int movementSpeed = 2;

    private int targetx = this.GetWorldPosition( ).getX( );
    private int targety = this.GetWorldPosition( ).getY( );

    private boolean canMove = true;

    public Projectile( Tile tile, SpriteType spritetype, String name, String description, int value, Map< DamageType, Integer > dmg, Direction dir )
    {
        super( tile, spritetype, name, description, value, ItemRarity.Generic );
        this.direction = dir;
        this.damage = new LinkedHashMap< DamageType, Integer >( dmg );
    }

    public void tick( )
    {

        if ( shot == false ) return;

        // "animate"
        UpdatePosition( );

        // calculate next step
        if ( canMove ) MoveForward( );

        // if the tile is not yet discovered
        // hide this projectile.
        boolean f = World.instance.GetTileAtPosition( this.GetTilePosition( ) ).discovered;
        if ( f == false ) this.tileDiscovered = false;
    }

    public void render( Graphics g )
    {

        if ( tileDiscovered == false ) { return; }
        else if ( alive && hidden == false )
        {
            Renderer.RenderSprite( sprite, this.GetWorldPosition( ), direction, g );
        }
        else if ( alive && discovered && hidden )
        {
            Renderer.RenderSprite( Util.tint( sprite, true ), this.GetWorldPosition( ), direction, g );
        }
    }

    private void UpdatePosition( )
    {
        int x = this.GetWorldPosition( ).getX( );
        int y = this.GetWorldPosition( ).getY( );

        // smooth movement
        if ( x < targetx - movementSpeed || x > targetx + movementSpeed )
        {

            if ( targetx < x ) { this.SetWorldPosition( x - movementSpeed, y ); }
            else if ( targetx > x ) this.SetWorldPosition( x + movementSpeed, y );

            canMove = false;

        }
        else if ( y < targety - movementSpeed || y > targety + movementSpeed )
        {

            if ( targety < y ) { this.SetWorldPosition( x, y - movementSpeed ); }
            else if ( targety > y ) this.SetWorldPosition( x, y + movementSpeed );

            canMove = false;

        }
        else
        {

            // force move the actor to the exact tile's position.
            this.SetWorldPosition( targetx, targety );

            canMove = true;
        }
    }

    private void MoveForward( )
    {

        Tile tile = World.instance.GetTileFromDirection( this.GetTilePosition( ), this.direction );

        if ( Util.isTileValid( tile ) )
        {

            // we are no longer on the last tile
            Tile lastTile = World.instance.GetTileAtPosition( this.GetTilePosition( ) );
            lastTile.RemoveItem( this );

            int x = tile.GetTilePosition( ).getX( );
            int y = tile.GetTilePosition( ).getY( );

            // update our tile position
            this.SetTilePosition( x, y );

            // update our world position
            this.targetx = tile.GetWorldPosition( ).getX( );
            this.targety = tile.GetWorldPosition( ).getY( );

            // set the tile's actor to be this.
            tile.AddItem( this );

        }
        else if ( tile.GetActor( ) != null )
        {

            DamageHandler.ActorTakeDamage( tile, damage );

            alive = false;
            Remove( );

        }
        else
        {

            // create effect
            if ( tile.isDiscovered( ) ) VisualEffectCreator.CreateHitEffect( tile );

            alive = false;
            Remove( );
        }
    }

    public void use( )
    {

        // if player
        // -> remove from inventory

        //this.shot = true;
    }

    public Map< DamageType, Integer > getDamage( ) { return this.damage; }

    public boolean isShot( ) { return shot; }
}
