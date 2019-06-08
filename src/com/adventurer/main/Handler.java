package com.adventurer.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.Equipment;
import com.adventurer.data.Experience;
import com.adventurer.data.ItemBonus;
import com.adventurer.data.Offense;
import com.adventurer.data.Resistances;
import com.adventurer.data.Stats;
import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.GUIState;
import com.adventurer.enumerations.WorldType;
import com.adventurer.gameobjects.Actor;
import com.adventurer.gameobjects.Armor;
import com.adventurer.gameobjects.Bomb;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.VisualEffect;
import com.adventurer.gameobjects.Enemy;
import com.adventurer.gameobjects.GameObject;
import com.adventurer.gameobjects.Gold;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Projectile;
import com.adventurer.gameobjects.Shrine;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Weapon;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Handler
{

    private List< GameObject > objects = new ArrayList< GameObject >( );

    // TODO: mouse hover --- > this is bad.
    private Tile hoverTile = null;
    private Coordinate mousePosition = new Coordinate( 0, 0 );

    // GUI cursor positions
    private int inventoryCursorPos = 0;
    private int equipmentCursorPos = 0;

    // flags to show different GUI-elements.
    private boolean showStats = false;
    private boolean showItemInspect = false;

    public static Handler instance;

    public Handler( )
    {
        if ( instance != null ) return;
        instance = this;
    }

    public void tick( )
    {
        for ( int i = 0; i < getObjects( ).size( ); i++ )
        {
            GameObject current = getObjects( ).get( i );
            if ( current != null ) current.tick( );
        }
    }

    // Draw order/queue:
    // 0. background (done in game.mainloop)
    // 1. tiles
    // 2. vanity items (blood etc.)
    // 3. items
    // 4. actors
    // 5. effects
    // 6. GUI

    // TODO: move some of these to Renderer, mainly renderGUI() ?

    public void render( Graphics g )
    {
        renderTiles( g );
        renderItems( g );
        renderActors( g );
        renderEffects( g );
        renderGUI( g );
    }

    private void renderTiles( Graphics g )
    {
        for ( int i = 0; i < getObjects( ).size( ); i++ )
        {
            GameObject current = getObjects( ).get( i );
            if ( current == null ) continue;
            if ( current instanceof Tile ) current.render( g );
        }
    }

    private void renderItems( Graphics g )
    {
        for ( int i = 0; i < getObjects( ).size( ); i++ )
        {
            GameObject current = getObjects( ).get( i );
            if ( current == null ) continue;
            if ( current instanceof Item ) current.render( g );
        }
    }

    private void renderActors( Graphics g )
    {
        for ( int i = 0; i < getObjects( ).size( ); i++ )
        {
            GameObject current = getObjects( ).get( i );
            if ( current == null ) continue;
            if ( current instanceof Actor ) current.render( g );
        }
    }

    private void renderEffects( Graphics g )
    {
        for ( int i = 0; i < getObjects( ).size( ); i++ )
        {
            GameObject current = getObjects( ).get( i );
            if ( current == null ) continue;
            if ( current instanceof VisualEffect ) current.render( g );
        }
    }

    private void renderGUI( Graphics g )
    {

        // ---------------------- RENDERS INGAME GUI! ---------------------------

        // get player
        Player player = ActorManager.GetPlayerInstance( );

        if ( Camera.instance == null || player == null ) return;

        // get camera
        Rectangle cam = Camera.instance.getCameraBounds( );
        Graphics2D g2d = ( Graphics2D ) g;

        // ------------ DRAW GUI --------------

        // calculate all positions
        // stats pos
        int stats_yPos = ( int ) cam.getMaxY( ) - 75;
        int stats_xPos = ( int ) cam.getMinX( ) + 50;
        Coordinate stats_coord = new Coordinate( stats_xPos, stats_yPos );

        // dungeon info i.e. name
        int dungeoninfo_yPos = ( int ) cam.getMinY( ) + 25;
        int dungeoninfo_xPos = ( int ) cam.getMinX( ) + 50;
        Coordinate dungeonInfo_coord = new Coordinate( dungeoninfo_xPos, dungeoninfo_yPos );

        // player info
        int charinfo_yPos = ( int ) cam.getMinY( ) + 20;
        int charinfo_xPos = ( int ) cam.getMinX( ) + 50;
        Coordinate chainfo_coord = new Coordinate( charinfo_xPos, charinfo_yPos );

        // hover tile info
        int tileinfo_yPos = mousePosition.getY( ) - 10;
        int tileinfo_xPos = mousePosition.getX( ) + 20;
        Coordinate tileinfo_coord = new Coordinate( tileinfo_xPos, tileinfo_yPos );

        // inventory position
        int inventory_yPos = ( int ) cam.getMinY( ) + 25;
        int inventory_xPos = ( int ) cam.getMaxX( ) - 150;
        Coordinate inventory_coord = new Coordinate( inventory_xPos, inventory_yPos );

        // equipment position
        int equipment_yPos = ( int ) cam.getMinY( ) + 175;
        int equipment_xPos = ( int ) cam.getMaxX( ) - 150;
        Coordinate equipment_coord = new Coordinate( equipment_xPos, equipment_yPos );

        // help text position
        int help_yPos = ( int ) cam.getMaxY( ) - 15;
        int help_xPos = ( int ) cam.getMinX( ) + 100;
        Coordinate help_coord = new Coordinate( help_xPos, help_yPos );

        // item inspect position
        int inspect_yPos = ( int ) cam.getMinY( ) + 25;
        int inspect_xPos = ( int ) cam.getMaxX( ) - 350;
        Coordinate inspect_coord = new Coordinate( inspect_xPos, inspect_yPos );

        // -------------------- Show help and cursors --------------------------

        if ( Game.instance.getGUIState( ) == GUIState.None )
        {

            // render general help
            Renderer.renderString(
                    "Play mode: Move/attack WASD, Inventory: I, Equipment: E, Character sheet: C, Mouse hover: info",
                    help_coord, Color.gray, Game.BASE_FONT_SIZE, g2d
            );

        }
        else if ( Game.instance.getGUIState( ) == GUIState.Inventory )
        {

            int stepSize = this.getInventoryCursorPos( ) * ( Game.BASE_FONT_SIZE + Game.LINE_HEIGHT );
            int yPos = inventory_coord.getY( ) + Game.BASE_FONT_SIZE + 1 + ( Game.LINE_HEIGHT * 2 );

            // render inventory cursor
            Renderer.renderRect(
                    new Coordinate( inventory_coord.getX( ), stepSize + yPos ),
                    new Coordinate( 120, Game.BASE_FONT_SIZE ),
                    Color.white, Color.white, true, g2d
            );

            // render inventory help
            Renderer.renderString(
                    "Inventory mode: Move cursor up: W, down: S, Equip/Use item: E, Inspect: I, Drop item: R, Exit: ESC",
                    help_coord, Color.gray, Game.BASE_FONT_SIZE, g2d
            );

        }
        else if ( Game.instance.getGUIState( ) == GUIState.Equipment )
        {

            int stepSize = this.getEquipmentCursorPos( ) * ( Game.BASE_FONT_SIZE + Game.LINE_HEIGHT );
            int yPos = equipment_coord.getY( ) + Game.BASE_FONT_SIZE + 1 + ( Game.LINE_HEIGHT * 2 );

            // render equipment cursor
            Renderer.renderRect(
                    new Coordinate( equipment_coord.getX( ), stepSize + yPos ),
                    new Coordinate( 120, Game.BASE_FONT_SIZE ),
                    Color.white, Color.white, true, g2d
            );

            // render equipment help
            Renderer.renderString(
                    "Equipment mode: Move cursor up: W, down: S, Unequip item: E, Inspect: I, Exit: ESC",
                    help_coord, Color.gray, Game.BASE_FONT_SIZE, g2d
            );

        }

        // -------------------- INSPECT ITEM --------------------

        if ( showItemInspect && Game.instance.getGUIState( ) == GUIState.Equipment )
        {

            int pos = this.getEquipmentCursorPos( );
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

            if ( item != null ) drawItemInspectInfo( item, inspect_coord, g2d );

        }
        else if ( showItemInspect && Game.instance.getGUIState( ) == GUIState.Inventory )
        {

            Item item = player.getInventory( ).getItemOnPosition( Handler.instance.getInventoryCursorPos( ) );

            if ( item != null ) drawItemInspectInfo( item, inspect_coord, g2d );

        }

        // -------------- LOCATION ---------------------

        if ( showStats == false )
        {

            // render location tag.
            Renderer.renderString( "Location: ", dungeonInfo_coord, Color.white, Game.BASE_FONT_SIZE, g2d );

            // render dungeon name and level
            if ( World.instance.getWorldType( ) == WorldType.Predefined )
            {

                Renderer.renderString(
                        "Chilly lobby",
                        new Coordinate( dungeonInfo_coord.getX( ) + 50, dungeonInfo_coord.getY( ) ),
                        Color.gray, Game.BASE_FONT_SIZE, g2d
                );

            }
            else if ( World.instance.getWorldType( ) == WorldType.Random )
            {

                if ( Game.instance.getCurrentSession( ) != null )
                {

                    Renderer.renderString(
                            "Dungeon (lvl. " + Game.instance.getCurrentSession( ).getDungeonLevel( ) + ")",
                            new Coordinate( dungeonInfo_coord.getX( ) + 50, dungeonInfo_coord.getY( ) ),
                            Color.gray, Game.BASE_FONT_SIZE, g2d
                    );

                }
                else
                {

                    Renderer.renderString(
                            "DEBUGGING",
                            new Coordinate( dungeonInfo_coord.getX( ) + 50, dungeonInfo_coord.getY( ) ),
                            Color.gray, Game.BASE_FONT_SIZE, g2d
                    );

                }

            }
        }

        // ---------------------- VITALS ------------------------

        if ( showStats == false )
        {
            // render vitals tag
            Renderer.renderString( "Vitals", stats_coord, Color.white, Game.BASE_FONT_SIZE, g2d );

            // render vitals (HP etc.)
            Renderer.renderString(
                    "\nHP: " + Util.generateRichTextForColor( Game.GUI_HEALTH, player.getHealth( ).GetCurrentHealth( ) ) + " / " + player.getHealth( ).GetMaxHP( ) + "\n" +
                            "MP: " + Util.generateRichTextForColor( Game.GUI_MANA, player.getMana( ).GetCurrentMana( ) ) + " / " + player.getMana( ).GetMaxMP( ) + "\n",
                    stats_coord,
                    Color.gray, Game.BASE_FONT_SIZE, g2d
            );
        }

        // ---------------------- STATS -------------------------

        if ( showStats )
        {

            // render character info tag
            Renderer.renderString( "CHARACTER", chainfo_coord, Color.white, Game.BASE_FONT_SIZE, g2d );

            Resistances resistances = player.getResistances( );
            Offense offense = player.getOffense( );
            Experience exp = player.getPlayerExperience( );
            Stats stats = player.getStats( );

            // render character info
            Renderer.renderString(
                    String.format(
                            "\nName: %s\n"
                                    + "Class: %s\n"
                                    + "Level: %d\n"
                                    + "Experience: %d / %d\n"
                                    + "-------- %s --------\n"
                                    + "Health: %s / %s\n"
                                    + "Mana:   %s / %s\n"
                                    + "-------- %s ---------\n"
                                    + "STR: %d (%d + %d)\n"
                                    + "VIT: %d (%d + %d)\n"
                                    + "INT: %d (%d + %d)\n"
                                    + "DEX: %d (%d + %d)\n"
                                    + "-------- %s --------\n"
                                    + "Melee: %d, Magic: %d, Ranged: %d\n"
                                    + "Physical:  %d\n"
                                    + "Fire:      %d\n"
                                    + "Frost:     %d\n"
                                    + "Shock:     %d\n"
                                    + "Holy:      %d\n"
                                    + "------ %s -----\n"
                                    + "Physical: %s\n"
                                    + "Fire:     %s\n"
                                    + "Frost:    %s\n"
                                    + "Shock:    %s\n"
                                    + "Holy:     %s\n",

                            player.getName( ),
                            player.getPlayerClass( ).toString( ),
                            exp.getCurrentLevel( ),
                            exp.getCurrentExp( ),
                            exp.getNeededExp( exp.getCurrentLevel( ) ),

                            Util.generateRichTextForColor( Color.white, "VITALS" ),
                            Util.generateRichTextForColor( Game.GUI_HEALTH, player.getHealth( ).GetCurrentHealth( ) ),
                            player.getHealth( ).GetMaxHP( ),
                            Util.generateRichTextForColor( Game.GUI_MANA, player.getMana( ).GetCurrentMana( ) ),
                            player.getMana( ).GetMaxMP( ),

                            Util.generateRichTextForColor( Color.white, "STATS" ),
                            stats.getSumStr( ),
                            stats.getBaseStrength( ),
                            stats.getAddedStr( ),

                            stats.getSumVit( ),
                            stats.getBaseVitality( ),
                            stats.getAddedVit( ),

                            stats.getSumInt( ),
                            stats.getBaseIntelligence( ),
                            stats.getAddedInt( ),

                            stats.getSumDex( ),
                            stats.getBaseDexterity( ),
                            stats.getAddedDex( ),

                            Util.generateRichTextForColor( Color.white, "DAMAGE" ),
                            Util.calcMeleeDamage( ),  // calculated from str
                            Util.calcMagicDamage( ),  // calculated from int
                            Util.calcRangedDamage( ), // calculated from dex

                            offense.getTotalMeleeDmgOfType( DamageType.Physical ), // total: weapon + stats
                            offense.getMeleeDmgOfType( DamageType.Fire ),
                            offense.getMeleeDmgOfType( DamageType.Frost ),
                            offense.getMeleeDmgOfType( DamageType.Shock ),
                            offense.getMeleeDmgOfType( DamageType.Holy ),

                            Util.generateRichTextForColor( Color.white, "RESISTANCES" ),
                            resistances.getPhysicalResistance( ),
                            resistances.getFireResistance( ),
                            resistances.getFrostResistance( ),
                            resistances.getShockResistance( ),
                            resistances.getHolyResistance( )

                    ), chainfo_coord, Color.gray, Game.BASE_FONT_SIZE, g2d
            );

        }

        // -------------------- INVENTORY ----------------------

        String invItems = "";
        int currentInvSpaces = player.getInventory( ).getInventoryItems( ).size( );
        int maxinvSpaces = player.getInventory( ).getMaxSize( );

        // populate inv items string
        if ( player.getInventory( ).getInventoryItems( ).isEmpty( ) )
        {

            // add '-' to fill the empty spaces.
            for ( int i = 0; i < maxinvSpaces; i++ ) { invItems += "-\n"; }

        }
        else
        {

            // first add the items into the list.
            for ( Item item : player.getInventory( ).getInventoryItems( ) )
            {

                // handle different items here.
                if ( item instanceof Gold )
                {

                    Gold gold = ( Gold ) item;
                    invItems += gold.getName( ) + " (" + gold.getAmount( ) + ")";

                }
                else { invItems += item.getName( ); }

                // add newlines
                invItems += "\n";
            }

            // then add '-' to fill the empty spaces.
            int count = maxinvSpaces - currentInvSpaces;
            for ( int i = 0; i < count; i++ ) { invItems += "-\n"; }
        }

        // render inventory tag
        Renderer.renderString(
                "Inventory " + "(" + currentInvSpaces + " / " + player.getInventory( ).getMaxSize( ) + ")",
                inventory_coord, Color.white, Game.BASE_FONT_SIZE, g2d
        );

        // render inventory items.
        Renderer.renderString( "\n" + invItems, inventory_coord, Color.gray, Game.BASE_FONT_SIZE, g2d );

        // -------------------- EQUIPMENT ---------------------

        String equipmentInfo = "";
        for ( Entry< String, Item > eq : player.getEquipment( ).getAllEquipment( ).entrySet( ) )
        {

            equipmentInfo += eq.getKey( ) + ": ";

            Item i = eq.getValue( );
            if ( i != null ) { equipmentInfo += eq.getValue( ).getName( ) + "\n"; }
            else { equipmentInfo += "None\n"; }
        }

        // render equipment tag.
        Renderer.renderString( "Equipment", equipment_coord, Color.white, Game.BASE_FONT_SIZE, g2d );

        // render equipment info.
        Renderer.renderString( "\n" + equipmentInfo, equipment_coord, Color.gray, Game.BASE_FONT_SIZE, g2d );

        // -------------------- MOUSE HOVER ---------------------
        if ( hoverTile != null )
        {

            // cache tile
            Tile cachedTile = hoverTile;

            String actorinfo = "-";
            String iteminfo = "-";
            String tileinfo = cachedTile.GetTileType( ).toString( );

            // get actor info
            if ( cachedTile.GetActor( ) != null )
            {
                Actor actor = cachedTile.GetActor( );
                if ( actor instanceof Enemy ) { actorinfo = ( ( Enemy ) actor ).toString( ); }
                else if ( actor instanceof Player ) actorinfo = ( ( Player ) actor ).toString( );
            }

            // get items info
            if ( cachedTile.GetItems( ).isEmpty( ) == false ) iteminfo = cachedTile.getItemsInfo( );

            // get tile info
            if ( cachedTile instanceof Shrine ) { tileinfo = ( ( Shrine ) cachedTile ).toString( ); }
            else if ( cachedTile instanceof Door ) tileinfo = ( ( Door ) cachedTile ).toString( );

            // format our complete string
            String txt = String.format( "Tile: %s\nActor: %s\nItem: %s",
                    /*cachedTile.GetTilePosition().toString(),*/ tileinfo, actorinfo, iteminfo );

            Renderer.renderString( txt, tileinfo_coord, Color.white, Game.BASE_FONT_SIZE, g2d );

        }
        else { Renderer.renderString( "", tileinfo_coord, Color.white, Game.BASE_FONT_SIZE, g2d ); }
    }

    private void drawItemInspectInfo( Item item, Coordinate inspect_coord, Graphics2D g2d )
    {

        // vars
        String slot = "-";
        int dmg_physical = 0, dmg_fire = 0, dmg_frost = 0, dmg_shock = 0, dmg_holy = 0,
                res_physical = 0, res_fire = 0, res_frost = 0, res_shock = 0, res_holy = 0,
                str = 0, dex = 0, intel = 0, vit = 0;

        // populate vars
        if ( item instanceof Armor )
        {

            Armor armor = ( Armor ) item;
            Map< DamageType, Integer > res = armor.getBonuses( ).getResistances( );
            ItemBonus ib = armor.getBonuses( );

            slot = armor.getSlot( ).toString( );

            if ( res.containsKey( DamageType.Fire ) ) res_fire = res.get( DamageType.Fire );
            if ( res.containsKey( DamageType.Frost ) ) res_frost = res.get( DamageType.Frost );
            if ( res.containsKey( DamageType.Shock ) ) res_shock = res.get( DamageType.Shock );
            if ( res.containsKey( DamageType.Holy ) ) res_holy = res.get( DamageType.Holy );
            if ( res.containsKey( DamageType.Physical ) ) res_physical = res.get( DamageType.Physical );

            str = ib.getStrBonus( );
            dex = ib.getDexBonus( );
            intel = ib.getIntBonus( );
            vit = ib.getVitBonus( );

        }
        else if ( item instanceof Weapon )
        {

            Weapon weapon = ( Weapon ) item;
            ItemBonus ib = weapon.getBonuses( );

            slot = weapon.getWeaponSlot( ).toString( );

            Map< DamageType, Integer > dmg = weapon.getBonuses( ).getDamage( );
            Map< DamageType, Integer > res = weapon.getBonuses( ).getResistances( );

            if ( dmg != null )
            {

                if ( dmg.containsKey( DamageType.Fire ) ) dmg_fire = dmg.get( DamageType.Fire );
                if ( dmg.containsKey( DamageType.Frost ) ) dmg_frost = dmg.get( DamageType.Frost );
                if ( dmg.containsKey( DamageType.Shock ) ) dmg_shock = dmg.get( DamageType.Shock );
                if ( dmg.containsKey( DamageType.Holy ) ) dmg_holy = dmg.get( DamageType.Holy );
                if ( dmg.containsKey( DamageType.Physical ) ) dmg_physical = dmg.get( DamageType.Physical );

            }

            if ( res != null )
            {

                if ( res.containsKey( DamageType.Fire ) ) res_fire = res.get( DamageType.Fire );
                if ( res.containsKey( DamageType.Frost ) ) res_frost = res.get( DamageType.Frost );
                if ( res.containsKey( DamageType.Shock ) ) res_shock = res.get( DamageType.Shock );
                if ( res.containsKey( DamageType.Holy ) ) res_holy = res.get( DamageType.Holy );
                if ( res.containsKey( DamageType.Physical ) ) res_physical = res.get( DamageType.Physical );

            }

            str = ib.getStrBonus( );
            dex = ib.getDexBonus( );
            intel = ib.getIntBonus( );
            vit = ib.getVitBonus( );

        }
        else if ( item instanceof Bomb )
        {

            Bomb bomb = ( Bomb ) item;

            for ( Entry< DamageType, Integer > e : bomb.getDamage( ).entrySet( ) )
            {

                DamageType key = e.getKey( );
                int val = e.getValue( );

                switch ( key )
                {
                    case Fire:
                        dmg_fire = val; break;
                    case Frost:
                        dmg_frost = val; break;
                    case Holy:
                        dmg_holy = val; break;
                    case Physical:
                        dmg_physical = val; break;
                    case Shock:
                        dmg_shock = val; break;
                    default:
                        break;
                }
            }

        }
        else if ( item instanceof Projectile )
        {

            Projectile proj = ( Projectile ) item;

            for ( Entry< DamageType, Integer > e : proj.getDamage( ).entrySet( ) )
            {

                DamageType key = e.getKey( );
                int val = e.getValue( );

                switch ( key )
                {
                    case Fire:
                        dmg_fire = val; break;
                    case Frost:
                        dmg_frost = val; break;
                    case Holy:
                        dmg_holy = val; break;
                    case Physical:
                        dmg_physical = val; break;
                    case Shock:
                        dmg_shock = val; break;
                    default:
                        break;
                }
            }

        }

        Color rarityColor = null;

        // decide rarity color
        switch ( item.getRarity( ) )
        {
            case Epic:
                rarityColor = Game.RARITYCOLOR_EPIC; break;
            case Generic:
                rarityColor = Game.RARITYCOLOR_GENERIC; break;
            case Legendary:
                rarityColor = Game.RARITYCOLOR_LEGENDARY; break;
            case Superior:
                rarityColor = Game.RARITYCOLOR_SUPERIOR; break;
            case Junk:
                rarityColor = Game.RARITYCOLOR_JUNK; break;
        }

        // create a string containing r, g and b values.
        String rarityColorStr = rarityColor.getRed( ) + "," + rarityColor.getGreen( ) + "," + rarityColor.getBlue( );
        String totalRarityString = "<color=\"" + rarityColorStr + "\">" + item.getRarity( ) + "</color>";

        // build string using the item's data
        String inspectInfo = String.format(
                "%s\n"
                        + "\'%s\'\n"
                        + "--------------------------------\n"
                        + "Value: %d gp\n"
                        + "Slot:  %s\n"
                        + "Rarity: %s\n"
                        + "--------------------------------\n",
                item.getName( ), item.getDescription( ), item.getValue( ),
                slot, totalRarityString
        );

        // control vars
        boolean addedDmg = false, addedRes = false;

        // add damage information
        if ( dmg_physical > 0 )
        {
            inspectInfo += "Physical damage: " + dmg_physical + "\n";
            addedDmg = true;
        }

        if ( dmg_fire > 0 )
        {
            inspectInfo += "Fire damage: " + dmg_fire + "\n";
            addedDmg = true;
        }

        if ( dmg_frost > 0 )
        {
            inspectInfo += "Frost damage: " + dmg_frost + "\n";
            addedDmg = true;
        }

        if ( dmg_shock > 0 )
        {
            inspectInfo += "Shock damage: " + dmg_shock + "\n";
            addedDmg = true;
        }

        if ( dmg_holy > 0 )
        {
            inspectInfo += "Holy damage: " + dmg_holy + "\n";
            addedDmg = true;
        }

        if ( addedDmg ) inspectInfo += "--------------------------------\n";

        // add resistance information
        if ( res_physical > 0 )
        {
            inspectInfo += "Physical resistance: " + res_physical + "\n";
            addedRes = true;
        }

        if ( res_fire > 0 )
        {
            inspectInfo += "Fire resistance: " + res_fire + "\n";
            addedRes = true;
        }

        if ( res_frost > 0 )
        {
            inspectInfo += "Frost resistance: " + res_frost + "\n";
            addedRes = true;
        }

        if ( res_shock > 0 )
        {
            inspectInfo += "Shock resistance: " + res_shock + "\n";
            addedRes = true;
        }

        if ( res_holy > 0 )
        {
            inspectInfo += "Holy resistance: " + res_holy + "\n";
            addedRes = true;
        }

        if ( addedRes ) inspectInfo += "--------------------------------\n";

        // add stat information
        if ( str > 0 ) inspectInfo += "Str: " + str + "\n";
        if ( dex > 0 ) inspectInfo += "Dex: " + dex + "\n";
        if ( vit > 0 ) inspectInfo += "Vit: " + vit + "\n";
        if ( intel > 0 ) inspectInfo += "Int: " + intel + "\n";

        // render item info
        Renderer.renderString( inspectInfo, inspect_coord, Color.white, Game.BASE_FONT_SIZE, g2d );

    }

    public void AddObject( GameObject go ) { this.getObjects( ).add( go ); }

    public void RemoveObject( GameObject go ) { this.getObjects( ).remove( go ); }

    public List< GameObject > getObjects( ) { return objects; }

    public void setObjects( List< GameObject > objects ) { this.objects = objects; }

    public Tile getHoverTile( ) { return hoverTile; }

    public void setHoverTile( Tile hoverTile ) { this.hoverTile = hoverTile; }

    public Coordinate getMousePosition( ) { return mousePosition; }

    public void setMousePosition( Coordinate mousePosition ) { this.mousePosition = mousePosition; }

    public boolean isShowingStats( ) { return showStats; }

    public void setShowStats( boolean showStats ) { this.showStats = showStats; }

    public int getInventoryCursorPos( ) { return inventoryCursorPos; }

    public void setInventoryCursorPos( int inventoryCursorPos ) { this.inventoryCursorPos = inventoryCursorPos; }

    public int getEquipmentCursorPos( ) { return equipmentCursorPos; }

    public void setEquipmentCursorPos( int equipmentCursorPos ) { this.equipmentCursorPos = equipmentCursorPos; }

    public boolean isShowItemInspect( ) { return showItemInspect; }

    public void setShowItemInspect( boolean showItemInspect ) { this.showItemInspect = showItemInspect; }

    public void moveInvCursorDown( )
    {
        int max = ActorManager.GetPlayerInstance( ).getInventory( ).getMaxSize( );
        if ( this.inventoryCursorPos == max - 1 ) { this.setInventoryCursorPos( 0 ); }
        else { this.setInventoryCursorPos( inventoryCursorPos + 1 ); }
    }

    public void moveInvCursorUp( )
    {
        int max = ActorManager.GetPlayerInstance( ).getInventory( ).getInventoryItems( ).size( );//ActorManager.GetPlayerInstance().getInventory().getMaxSize();
        if ( this.inventoryCursorPos == 0 ) { this.setInventoryCursorPos( max - 1 ); }
        else { this.setInventoryCursorPos( inventoryCursorPos - 1 ); }
    }

    public void moveEquipmentCursorDown( )
    {
        int max = 9;
        if ( this.equipmentCursorPos == max - 1 ) { this.setEquipmentCursorPos( 0 ); }
        else { this.setEquipmentCursorPos( equipmentCursorPos + 1 ); }
    }

    public void moveEquipmentCursorUp( )
    {
        int max = 9;
        if ( this.equipmentCursorPos == 0 ) { this.setEquipmentCursorPos( max - 1 ); }
        else { this.setEquipmentCursorPos( equipmentCursorPos - 1 ); }
    }
}
