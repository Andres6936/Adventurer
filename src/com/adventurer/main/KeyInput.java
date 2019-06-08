package com.adventurer.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.adventurer.data.Equipment;
import com.adventurer.enumerations.ArmorSlot;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.EGameState;
import com.adventurer.enumerations.GUIState;
import com.adventurer.enumerations.WeaponSlot;
import com.adventurer.gameobjects.Equippable;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Usable;

public class KeyInput extends KeyAdapter
{

    private List< Integer > buttons = new ArrayList<>( );

    KeyInput( ) {}

    public void keyPressed( KeyEvent e )
    {
        EGameState currentState = Game.instance.getGameState( );
        if ( currentState == EGameState.IN_GAME || currentState == EGameState.READY ) inGameKeys( e );
    }

    private void inGameKeys( KeyEvent e )
    {

        // get the pressed key
        int key = e.getKeyCode( );

        // when loading is finished, wait for player input to start game.
        if ( Game.instance.getGameState( ) == EGameState.READY )
        {
            Game.instance.setGameState( EGameState.IN_GAME );
            return;
        }

        Player player = ActorManager.GetPlayerInstance( );
        if ( player.getHealth( ).isDead( ) ) return;

        // -------------- HANDLE INPUTS ------------------

        // toggle keys
        if ( ! buttons.contains( key ) ) { buttons.add( key ); }
        else { return; }

        // cache in which gui state are we in.
        GUIState GUIState = Game.instance.getGUIState( );

        // decide what actions to do when in different GUI-states.
        if ( GUIState == com.adventurer.enumerations.GUIState.None )
        {

            // movement
            if ( key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8 ) { player.Move( Direction.North ); }
            else if ( key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2 ) { player.Move( Direction.South ); }
            else if ( key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4 ) { player.Move( Direction.West ); }
            else if ( key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6 ) player.Move( Direction.East );

            // Toggle inventory state
            if ( key == KeyEvent.VK_I )
            {
                Handler.instance.setInventoryCursorPos( 0 );
                Game.instance.setGUIState( com.adventurer.enumerations.GUIState.Inventory );
            }

            // Toggle equipment state
            if ( key == KeyEvent.VK_E )
            {
                Handler.instance.setEquipmentCursorPos( 0 );
                Game.instance.setGUIState( com.adventurer.enumerations.GUIState.Equipment );
            }

        }
        else if ( GUIState == com.adventurer.enumerations.GUIState.Inventory )
        {

            boolean success = false;

            // move cursor in inventory
            if ( key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8 || key == KeyEvent.VK_UP )
            {
                Handler.instance.moveInvCursorUp( );
            }
            else if ( key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2 || key == KeyEvent.VK_DOWN )
            {
                Handler.instance.moveInvCursorDown( );
            }

            // drop item
            if ( key == KeyEvent.VK_R )
            {
                Handler.instance.setShowItemInspect( false );
                Item item = player.getInventory( ).getItemOnPosition( Handler.instance.getInventoryCursorPos( ) );
                if ( item != null )
                {
                    success = true;
                    player.dropItem( item );
                }
            }

            // equip/use item
            if ( key == KeyEvent.VK_E || key == KeyEvent.VK_ENTER )
            {
                Handler.instance.setShowItemInspect( false );
                Item item = player.getInventory( ).getItemOnPosition( Handler.instance.getInventoryCursorPos( ) );
                if ( item != null )
                {

                    if ( item instanceof Equippable ) { player.getEquipment( ).equipItem( item ); }
                    else if ( item instanceof Usable ) ( ( Usable ) item ).use( );

                    success = true;
                }
            }

            // inspect item
            if ( key == KeyEvent.VK_I )
            {
                Item item = player.getInventory( ).getItemOnPosition( Handler.instance.getInventoryCursorPos( ) );
                if ( item != null )
                {
                    if ( Handler.instance.isShowItemInspect( ) ) { Handler.instance.setShowItemInspect( false ); }
                    else { Handler.instance.setShowItemInspect( true ); }
                }
            }

            // escape from inventory mode
            if ( key == KeyEvent.VK_ESCAPE )
            {
                Handler.instance.setShowItemInspect( false );
                Game.instance.setGUIState( com.adventurer.enumerations.GUIState.None );
            }

            // exit inventory mode automatically
            if ( Game.AUTOMATICALLY_ESCAPE_FROM_INV_MODE_AFTER_SUCCESS )
            {
                if ( success )
                {
                    Game.instance.setGUIState( com.adventurer.enumerations.GUIState.None );
                    Handler.instance.setShowItemInspect( false );
                }
            }

        }
        else if ( GUIState == com.adventurer.enumerations.GUIState.Equipment )
        {

            boolean success = false;

            // move cursor in inventory
            if ( key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8 || key == KeyEvent.VK_UP )
            {
                Handler.instance.moveEquipmentCursorUp( );
            }
            else if ( key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2 || key == KeyEvent.VK_DOWN )
            {
                Handler.instance.moveEquipmentCursorDown( );
            }

            // unequip item
            if ( key == KeyEvent.VK_E || key == KeyEvent.VK_ENTER )
            {

                Handler.instance.setShowItemInspect( false );

                int pos = Handler.instance.getEquipmentCursorPos( );
                Equipment eq = player.getEquipment( );

                if ( pos == 0 )
                {
                    eq.unequipSlot( WeaponSlot.Mainhand );
                    success = true;
                }
                else if ( pos == 1 )
                {
                    eq.unequipSlot( WeaponSlot.Offhand );
                    success = true;
                }
                else if ( pos == 2 )
                {
                    eq.unequipSlot( ArmorSlot.Head );
                    success = true;
                }
                else if ( pos == 3 )
                {
                    eq.unequipSlot( ArmorSlot.Chest );
                    success = true;
                }
                else if ( pos == 4 )
                {
                    eq.unequipSlot( ArmorSlot.Legs );
                    success = true;
                }
                else if ( pos == 5 )
                {
                    eq.unequipSlot( ArmorSlot.Feet );
                    success = true;
                }
                else if ( pos == 6 )
                {
                    eq.unequipSlot( ArmorSlot.Amulet );
                    success = true;
                }
                else if ( pos == 7 )
                {
                    eq.unequipSlot( ArmorSlot.Ring );
                    success = true;
                }
                else { System.out.println( "NO SUCH POSITION AVAILABLE!!" ); }

            }

            // inspect item
            if ( key == KeyEvent.VK_I )
            {

                int pos = Handler.instance.getEquipmentCursorPos( );
                Equipment eq = player.getEquipment( );
                Item item = null;

                if ( pos == 0 ) { item = eq.getMainHand( ); }
                else if ( pos == 1 ) { item = eq.getOffHand( ); }
                else if ( pos == 2 ) { item = eq.getHead( ); }
                else if ( pos == 3 ) { item = eq.getChest( ); }
                else if ( pos == 4 ) { item = eq.getLegs( ); }
                else if ( pos == 5 ) { item = eq.getFeet( ); }
                else if ( pos == 6 ) { item = eq.getAmulet( ); }
                else if ( pos == 7 ) item = eq.getRing( );

                if ( item != null )
                {
                    if ( Handler.instance.isShowItemInspect( ) ) { Handler.instance.setShowItemInspect( false ); }
                    else { Handler.instance.setShowItemInspect( true ); }
                }
            }

            // escape from equipment mode
            if ( key == KeyEvent.VK_ESCAPE )
            {
                Handler.instance.setShowItemInspect( false );
                Game.instance.setGUIState( com.adventurer.enumerations.GUIState.None );
            }

            // exit inventory mode automatically
            if ( Game.AUTOMATICALLY_ESCAPE_FROM_INV_MODE_AFTER_SUCCESS )
            {
                if ( success )
                {
                    Handler.instance.setShowItemInspect( false );
                    Game.instance.setGUIState( com.adventurer.enumerations.GUIState.None );
                }
            }
        }

        // ------ BELOW THIS LINE, ALL BUTTONS WORK WHATEVER GUI-STATE IS ON --------

        // toggle character panel
        if ( key == KeyEvent.VK_C )
        {
            if ( Handler.instance.isShowingStats( ) ) { Handler.instance.setShowStats( false ); }
            else { Handler.instance.setShowStats( true ); }
        }

    }

    public void keyReleased( KeyEvent e ) { buttons.clear( ); }
}
