package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Health;
import com.adventurer.data.Mana;
import com.adventurer.data.Offense;
import com.adventurer.data.PredefinedMaps;
import com.adventurer.data.Resistances;
import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.*;
import com.adventurer.utilities.Util;

public class Actor extends GameObject
{

    protected int targetx = this.GetWorldPosition( ).getX( ), targety = this.GetWorldPosition( ).getY( );

    protected BufferedImage directionArrow = null;

    protected Direction lookDir;
    protected Direction lastLookDir;

    protected boolean canMove = true;

    protected Health myHP;
    protected Mana myMP;
    protected Resistances myResistances;
    protected Offense myOffense;

    protected String name = "";
    protected int movementSpeed = 2; // how fast is the animation between tiles

    public Actor( Coordinate worldPos, Coordinate tilePos, SpriteType spritetype,
                  int maxHP, int maxMP, int meleeDamage, int rangedDamage, int magicDamage,
                  String name, int movementSpeed, Map< DamageType, Integer > resistances )
    {
        super( worldPos, tilePos, spritetype );

        // set stuff here
        this.lookDir = Direction.West;

        // set damage types
        this.myOffense = new Offense( meleeDamage, magicDamage, rangedDamage );

        // set other stuff
        this.name = name;
        this.movementSpeed = movementSpeed;
        this.myHP = new Health( maxHP );
        this.myMP = new Mana( maxMP );

        // set resistances
        if ( resistances == null || resistances.isEmpty( ) ) { this.myResistances = new Resistances( ); }
        else { this.myResistances = new Resistances( resistances ); }

        // register to tile
        World.instance.GetTileAtPosition( tilePos ).SetActor( this );
    }

    public void forceMove( Coordinate worldPos, Coordinate tilePos )
    {

        Tile tile = World.instance.GetTileAtPosition( tilePos );

        // update our tile position
        this.SetTilePosition( tilePos.getX( ), tilePos.getY( ) );

        // update our world position
        this.targetx = worldPos.getX( );
        this.targety = worldPos.getY( );

        this.SetWorldPosition( worldPos );

        // set the tile's actor to be this.
        tile.SetActor( this );
    }

    public void OnDeath( Tile tile )
    {

        if ( this instanceof Enemy )
        {

            Enemy enemy = ( Enemy ) this;

            // give player exp
            ActorManager.GetPlayerInstance( ).getPlayerExperience( ).addCurrentExp( enemy.getExp( ) );

            // TODO: spawn loot


            // remove gameobject
            Remove( );

        }
        else if ( this instanceof Player )
        {

            // TODO: death screen?
            // respawn player at lobby
            ActorManager.RemovePlayer( );
            World.instance.Remove( );
            new World( PredefinedMaps.GetLobby( ) );

        }
    }

    public void Remove( )
    {

        // get tile
        Tile tile = World.instance.GetTileAtPosition( this.GetTilePosition( ) );

        // set tile's actor to null
        // -> others can walk on the tile
        tile.SetActor( null );

        // remove this object from handler
        // -> no longer ticks
        Handler.instance.RemoveObject( this );

        // hide
        Hide( );
    }

    public void UseBomb( Tile tile )
    {

        if ( Util.isTileValid( tile ) )
        {

            if ( this instanceof Player )
            {

				/*Player player = (Player) this;
				Inventory inv = player.getInventory();*/

                // TODO: bombs?


            }
            else if ( this instanceof Enemy )
            {

				/*Enemy enemy = (Enemy) this;

				if(enemy.getEnemyType() == EnemyType.Maggot) {

				    // TODO: bomb --> egg?
					new Bomb(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Bomb01, 900, 150, BombType.Gas);

				} else {

					new Bomb(tile.GetWorldPosition(), tile.GetTilePosition(), SpriteType.Bomb01, 1500, 300, BombType.Normal);

				}*/
            }
        }
    }

    public void Shoot( Coordinate originTilePos, Direction direction, SpriteType projSpriteType )
    {

        // ----- this is ranged attack -----

        if ( this instanceof Player )
        {

            // TODO: player shoot

			/*Player player = (Player) this;
			Inventory inv = player.getInventory();

			if(inv.getProjectileCount() > 0) {

				Tile projStartTile = World.instance.GetTileAtPosition(originTilePos);
				new Projectile(projStartTile.GetWorldPosition(), projStartTile.GetTilePosition(), projSpriteType, rangedDamage, direction);

				inv.addProjectiles(-1);
			}*/

        }
        else
        {

            // get tile
            Tile projStartTile = World.instance.GetTileAtPosition( originTilePos );

            // create projectile
            Projectile p = ItemCreator.createProjectile( projStartTile, DamageType.Physical, 5, direction );

            // shoot projectile
            p.use( );
        }
    }

    public void Attack( Tile tile )
    {

        // ----- this is melee attack ------

        // get actor
        GameObject object = tile.GetActor( );

        // do damage
        if ( object != null ) DamageHandler.ActorTakeDamage( tile, this.myOffense.getTotalMeleeDmg( ) );

    }

    protected void UpdatePosition( )
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

    public void render( Graphics g ) {}

    public void tick( ) {}

    public void Move( Direction dir ) {}

    public String toString( ) { return this.name; }

    public Tile getCurrentTile( ) { return World.instance.GetTileAtPosition( this.GetTilePosition( ) ); }

    public Direction GetLookDirection( ) { return this.lookDir; }

    public void SetLookDirection( Direction dir ) { this.lookDir = dir; }

    public String getName( ) { return this.name; }

    public Health getHealth( ) { return this.myHP; }

    public Mana getMana( ) { return this.myMP; }

    public Resistances getResistances( ) { return this.myResistances; }

    public Offense getOffense( ) { return this.myOffense; }

    public Rectangle GetBounds( ) { return null; }
}
