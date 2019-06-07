package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Equipment;
import com.adventurer.data.Experience;
import com.adventurer.data.Inventory;
import com.adventurer.data.Offense;
import com.adventurer.data.PredefinedMaps;
import com.adventurer.data.Session;
import com.adventurer.data.Stats;
import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.GameState;
import com.adventurer.enumerations.KeyType;
import com.adventurer.enumerations.PlayerClass;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.main.*;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Player extends Actor
{

    private LoSManager losmanager;
    private Inventory inventory;
    private Equipment equipment;
    private Stats stats;
    private Experience playerExperience;
    private PlayerClass playerClass;

    public Player( Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, Map< DamageType, Integer > resistances )
    {
        super( worldPos, tilePos, spritetype,
               Game.PLAYER_START_BASE_HEALTH, Game.PLAYER_START_BASE_MANA, 0, 0, 0, "Player", 2, resistances );

        this.stats = new Stats( );
        this.losmanager = new LoSManager( );
        this.inventory = new Inventory( this );
        this.equipment = new Equipment( );
        this.playerExperience = new Experience( );

        // TODO: class chooser
        this.playerClass = PlayerClass.Warrior;

        // calculate health and mana
        updateHPandMP( );

        // update damage too
        updateDmg( );
    }

    public void tick( )
    {
        if ( myHP == null ) return;
        if ( ! myHP.isDead( ) )
        {

            UpdatePosition( );
            if ( Game.CALCULATE_PLAYER_LOS && losmanager != null ) losmanager.CalculateLos( this.GetTilePosition( ) );

        }
        else
        {

            // save current session data.
            if ( Game.instance.getCurrentSession( ) != null )
            {
                Game.instance.getCurrentSession( ).saveSessionData( );
                Game.instance.setCurrentSession( null );
            }
            else { System.out.println( "No currentSession (Player.Tick)." ); }

            // die...
            OnDeath( World.instance.GetTileAtPosition( this.GetTilePosition( ) ) );
        }
    }

    public void render( Graphics g )
    {
        Renderer.RenderSprite( sprite, this.GetWorldPosition( ), g );
    }

    public void Move( Direction dir )
    {

        if ( ! canMove ) return;

        Tile tile = World.instance.GetTileFromDirection( this.GetTilePosition( ), dir );

        if ( tile == null )
        {
            System.out.println( "TILE IS NULL!" );
            return;
        }

        if ( Util.isTileValid( tile ) )
        {

            // we are no longer on the last tile
            Tile lastTile = World.instance.GetTileAtPosition( this.GetTilePosition( ) );
            lastTile.SetActor( null );

            int x = tile.GetTilePosition( ).getX( );
            int y = tile.GetTilePosition( ).getY( );

            // update our tile position
            this.SetTilePosition( x, y );

            // update our world position
            this.targetx = tile.GetWorldPosition( ).getX( );
            this.targety = tile.GetWorldPosition( ).getY( );

            // set the tile's actor to be this.
            tile.SetActor( this );

            // set off trap
            if ( tile instanceof Trap ) ( ( Trap ) tile ).activate( );

            // set off shrine
            if ( tile instanceof Shrine ) ( ( Shrine ) tile ).activate( );

            // pickup items
            if ( ! tile.GetItems( ).isEmpty( ) )
            {
                List< Item > temp = new ArrayList<>( tile.GetItems( ) );
                for ( Item i : temp ) pickUpItem( i );
            }

        }
        else if ( Util.doesTileHaveChest( tile ) )
        {

            // if there is chest on the tile.
            Chest chest = Util.getChest( tile );

            if ( chest.isLocked( ) )
            {

                Key key = inventory.getKey( KeyType.Normal );

                if ( key != null )
                {
                    chest.Unlock( );
                    inventory.removeItemFromInventory( key );
                    key.Remove( );
                }

            }
            else
            {

                chest.Open( );

            }

        }
        else if ( tile instanceof Door )
        {

            Door door = ( Door ) tile;

            if ( door.isLocked( ) && door.getDoorType( ) == DoorType.Normal && door.GetTileType( ) == TileType.LockedDoor )
            {

                Key key = inventory.getKey( KeyType.Normal );

                if ( key != null )
                {
                    door.Unlock( );
                    inventory.removeItemFromInventory( key );
                    key.Remove( );
                }

            }
            else if ( door.isLocked( ) && door.getDoorType( ) == DoorType.Diamond )
            {

                Key key = inventory.getKey( KeyType.Diamond );

                if ( key != null )
                {
                    door.Unlock( );
                    inventory.removeItemFromInventory( key );
                    key.Remove( );
                }

            }
            else if ( ! door.isLocked( ) ) door.Open( );

        }
        else if ( tile.GetActor( ) != null )
        {

            if ( tile.GetActor( ) instanceof Enemy ) Attack( tile );

        }
        else if ( tile instanceof Portal )
        {

            Portal portal = ( Portal ) tile;

            if ( portal.isExit( ) )
            {

                // exit dungeon -> return to lobby.

                // set game state to loading immediately.
                Game.instance.setGameState( GameState.Loading );

                // save session
                Game.instance.getCurrentSession( ).saveSessionData( );
                Game.instance.setCurrentSession( null );

                // delete current world
                World.instance.Remove( );

                // create lobby.
                new World( PredefinedMaps.GetLobby( ) );

            }
            else
            {

                if ( Game.instance.getCurrentSession( ) == null )
                {
                    Game.instance.setCurrentSession( new Session( "session_" + System.currentTimeMillis( ) ) );
                }
                else { Game.instance.getCurrentSession( ).addDungeonLevel( 1 ); }

                World.instance.Remove( );
                new World( Game.ROOM_COUNT );
            }

        }
        else
        {
            // TODO: interaction with destructible tiles.
            tile.GetTileType( );
        }
    }

    private void pickUpItem( Item item )
    {
        if ( ! this.inventory.isFull( ) )
        {
            this.inventory.addToInventory( item );
            item.Remove( );
        }
    }

    public void dropItem( Item item )
    {
        if ( this.inventory.getInventoryItems( ).contains( item ) )
        {
            item.moveItemTo( World.instance.GetTileAtPosition( this.GetTilePosition( ) ) );
            this.inventory.removeItemFromInventory( item );
            Handler.instance.AddObject( item );
        }
        else
        {
            item.moveItemTo( World.instance.GetTileAtPosition( this.GetTilePosition( ) ) );
            Handler.instance.AddObject( item );
        }
    }

    public void updateHPandMP( )
    {

        // calculate health and mana
        int health = Util.calcHealth( this.getStats( ).getSumVit( ) );
        int mana = Util.calcMana( this.getStats( ).getSumInt( ) );

        // update them in Health, Mana, Offense and Resistances.
        this.getHealth( ).setMaxHP( health );
        this.getMana( ).setMaxMP( mana );
    }

    private void updateDmg( )
    {
        Offense offense = this.getOffense( );
        offense.setTotalMeleeDmgOfType( DamageType.Physical, Util.calcMeleeDamage( this.stats.getSumStr( ) ) );
    }

    public String toString( ) { return "You, our hero."; }

    public Inventory getInventory( ) { return this.inventory; }

    public Stats getStats( ) { return this.stats; }

    public Equipment getEquipment( ) { return equipment; }

    public PlayerClass getPlayerClass( ) { return playerClass; }

    public Experience getPlayerExperience( ) { return playerExperience; }

}
