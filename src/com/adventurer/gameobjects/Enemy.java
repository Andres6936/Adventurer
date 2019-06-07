package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.EnemyType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.*;
import com.adventurer.utilities.AStar;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Enemy extends Actor
{

    private EnemyType enemyType;
    private SpriteType projectileType;
    private EnemyLoSManager losManager;

    private boolean hasRangedAttack = false;
    private int exp = 0;

    private int moveCooldownBase = 1000;
    private long moveTimer = 0;

    private Tile lastPlayerPosition = null;

    public Enemy( Coordinate worldPos, Coordinate tilePos,
                  EnemyType enemytype, SpriteType spritetype,
                  int maxHP, int maxMP, int meleeDmg, int rangedDmg, int magicDmg,
                  String name, boolean isRanged, int movementSpeed, Map< DamageType, Integer > resistances, int moveCooldownBase, int exp )
    {
        super( worldPos, tilePos, spritetype, maxHP, maxMP, meleeDmg, rangedDmg, magicDmg, name, movementSpeed, resistances );

        // declare ranged units here
        this.hasRangedAttack = isRanged;

        // how fast the enemy can do actions.
        this.moveCooldownBase = moveCooldownBase;

        // TODO: different projectiles?
        this.projectileType = SpriteType.Arrow01;

        // set exp gained
        this.exp = exp;

        this.setEnemyType( enemytype );
        this.losManager = new EnemyLoSManager( );
        this.moveTimer = System.currentTimeMillis( );

    }

    public void render( Graphics g )
    {
        if ( hidden == false && discovered ) Renderer.RenderSprite( sprite, this.GetWorldPosition( ), g );
    }

    public void tick( )
    {

        if ( ActorManager.GetPlayerInstance( ) == null ) return;

        if ( myHP.isDead( ) == false )
        {

            // only calculate discovered enemies behavior.
            if ( this.discovered == false ) return;

            long current = System.currentTimeMillis( );

            if ( current > moveTimer && canMove )
            {

                // ***** Simple AI *****
                // * check if player is nearby then attack
                // * else just move randomly.
                // *********************

                // calculate line of sight
                Tile[] visibleTiles = losManager.GetVisibleTiles( this.GetTilePosition( ) );

                boolean canSeePlayer = false;

                // search for player
                // -> updates lastPlayerPosition if player is seen.
                for ( Tile tile : visibleTiles )
                {
                    if ( tile.GetActor( ) instanceof Player )
                    {
                        lastPlayerPosition = tile;
                        canSeePlayer = true;
                        break;
                    }
                }

                if ( Game.DRAW_ENEMY_FOV )
                {
                    for ( Tile tile : World.instance.GetTiles( ) ) tile.Deselect( );
                    for ( Tile tile : visibleTiles ) tile.Select( );
                }

                if ( lastPlayerPosition != null )
                {

                    // --------------- ATTACK MODE ----------------

                    Tile currentTile = World.instance.GetTileAtPosition( this.GetTilePosition( ) );
                    List< Tile > path = AStar.CalculatePath( currentTile, lastPlayerPosition );

                    if ( path == null || path.isEmpty( ) )
                    {
                        // NO PATH AVAILABLE!
                        lastPlayerPosition = null;
                        return;
                    }

                    // https://stackoverflow.com/questions/3962766/how-to-reverse-a-list-in-java
                    List< Tile > pathCopy = path.subList( 0, path.size( ) );
                    Collections.reverse( pathCopy );

                    Tile nextStep = null;

                    try
                    {
                        nextStep = path.get( 0 );
                    }
                    catch ( ArrayIndexOutOfBoundsException e )
                    {
                        e.printStackTrace( );
                        System.exit( 1 );
                    }

                    if ( Game.DRAW_ENEMY_PATH )
                    {
                        for ( Tile tile : World.instance.GetTiles( ) ) tile.Deselect( );
                        for ( Tile tile : path ) tile.Select( );
                    }

                    if ( nextStep == null )
                    {
                        System.out.println( "NEXT STEP IS NULL!" );
                        return;
                    }

                    Direction dir = World.instance.GetDirectionOfTileFromPoint( currentTile, nextStep );

                    // Decide whether to shoot, melee or move.
                    if ( this.hasRangedAttack && canSeePlayer &&
                            ( currentTile.GetTilePosition( ).getX( ) == lastPlayerPosition.GetTilePosition( ).getX( ) ||
                                    currentTile.GetTilePosition( ).getY( ) == lastPlayerPosition.GetTilePosition( ).getY( ) ) )
                    {

                        // --------------- USE RANGED ATTACK ---------------
                        Shoot( this.GetTilePosition( ), dir, this.projectileType );

                    }
                    else
                    {

                        // --------------- MOVE & MELEE ATTACK ---------------
                        Move( dir );
                    }

                }
                else
                {

                    // -------------- RANDOM MODE -----------------

                    // randomize direction
                    Direction randomDir = Util.GetRandomCardinalDirection( );

                    // randomly shoot projectile
                    if ( Util.GetRandomInteger( ) > 80 && hasRangedAttack )
                    {

                        Shoot( this.GetTilePosition( ), randomDir, projectileType );

                    }
                    else
                    {

                        // move
                        Move( randomDir );

                    }

                }

                // update timer
                moveTimer = current + moveCooldownBase + Util.GetRandomInteger( 0, 500 );
            }

            UpdatePosition( );

        }
        else
        {
            OnDeath( World.instance.GetTileAtPosition( this.GetTilePosition( ) ) );
        }
    }

    public void Move( Direction dir )
    {

        Tile tile = World.instance.GetTileFromDirection( this.GetTilePosition( ), dir );
        World world = World.instance.GetWorld( );

        // update facing
        lookDir = dir;

        if ( Util.isTileValid( tile ) )
        {

            // tile is our new tile
            world.GetTileAtPosition( this.GetTilePosition( ) ).SetActor( null );

            int x = tile.GetTilePosition( ).getX( );
            int y = tile.GetTilePosition( ).getY( );

            // update our tile position
            this.SetTilePosition( x, y );

            // update our world position
            targetx = tile.GetWorldPosition( ).getX( );
            targety = tile.GetWorldPosition( ).getY( );

            // set the tile's actor to be this.
            tile.SetActor( this );

            // hide ourselves if we are on a hidden tile.
            if ( tile.isHidden( ) )
            {
                this.Hide( );
            }

            // set off trap
            if ( tile instanceof Trap )
            {
                ( ( Trap ) tile ).activate( );
            }

        }
        else if ( tile instanceof Door )
        {

            // open door
            ( ( Door ) tile ).Open( );

        }
        else if ( tile.GetActor( ) != null )
        {

            if ( tile.GetActor( ) instanceof Player ) Attack( tile );

        }
    }

    public String toString( )
    {
        String info = super.toString( );
        return info + " (" + this.enemyType + ")" + ", HP:" + this.getHealth( ).GetCurrentHealth( );
    }

    public EnemyType getEnemyType( ) { return enemyType; }

    public void setEnemyType( EnemyType enemyType ) { this.enemyType = enemyType; }

    public int getExp( ) { return exp; }

    public void setExp( int exp ) { this.exp = exp; }
}
