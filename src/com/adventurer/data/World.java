package com.adventurer.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.EGameState;
import com.adventurer.enumerations.RoomType;
import com.adventurer.enumerations.ShrineType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.enumerations.TrapType;
import com.adventurer.enumerations.WorldType;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Portal;
import com.adventurer.gameobjects.Shrine;
import com.adventurer.gameobjects.Tile;
import com.adventurer.gameobjects.Trap;
import com.adventurer.main.ActorManager;
import com.adventurer.main.Game;
import com.adventurer.main.ItemCreator;
import com.adventurer.main.SpriteCreator;
import com.adventurer.utilities.DungeonGeneration;
import com.adventurer.utilities.Util;

public class World
{

    private int worldHeight;
    private int worldWidth;
    private List< Tile > tiles;
    private List< Room > rooms;
    private WorldType worldType;
    public static World instance;

    // ------ Constructors --------

    // creates a predefined map
    public World( char[][] map )
    {

        // init world
        initiation( );

        worldType = WorldType.Predefined;

        // generate dungeon + return player spawn tile
        Tile spawnTile = CreatePredefinedMap( map );

        // create/move player
        createPlayer( spawnTile );

        // set state.
        Game.instance.setGameState( EGameState.READY );
    }

    // create a randomized dungeon level
    public World( int roomcount )
    {

        // init world
        initiation( );

        worldType = WorldType.Random;

        // generate dungeon
        this.tiles = DungeonGeneration.createDungeon( roomcount );

        // fill dungeon with stuff.
        for ( Room room : rooms )
        {

            // actors and exit
            if ( room.getRoomType( ) == RoomType.PlayerStartRoom )
            {

                // ----------------------- Player spawn room ------------------

                Tile tile = Util.getRandomTile( room.getTiles( ) );

                // change tile sprite
                tile.SetSprite( SpriteCreator.instance.CreateSprite( SpriteType.SpawnTile01 ) );

                // create/move player
                createPlayer( tile );

                // create health shrine
                Tile[] mods = createHealthShrine( room.getTiles( ) );

                // mods[0] == shrine tile
                // mods[1] == removed tile
                room.getTiles( ).remove( mods[ 1 ] );
                room.getTiles( ).add( mods[ 0 ] );

                // Do not randomize anything.
                continue;

                // -----------------------------------------------------------

            }
            else if ( room.getRoomType( ) == RoomType.DungeonExitRoom )
            {

                // don't spawn anything!
                continue;

            }
            else
            {

                // create enemies.
                ActorManager.CreateEnemies( Util.GetRandomInteger( 0, 5 ), room.getTiles( ) );
            }

            // create chests in treasure rooms.
            if ( room.getRoomType( ) == RoomType.Treasure )
            {
                for ( int i = 0; i < Util.GetRandomInteger( 1, 3 ); i++ )
                {

                    // get randomized & valid tile
                    Tile tile = null;
                    do
                    {
                        tile = Util.getRandomTile( room.getTiles( ) );
                        if ( tile == null ) break;
                    } while ( Util.isTileValid( tile ) == false );

                    // create chest
                    if ( tile != null ) { ItemCreator.CreateChest( tile, true ); }
                    else { System.out.println( "Failed to create chest: no free tile available!" ); }
                }
            }
        }

        // set state
        Game.instance.setGameState( EGameState.READY );
    }

    // ---------------------------

    private Tile[] createHealthShrine( List< Tile > tiles )
    {

        // get random valid tile
        Tile tile = Util.getRandomTile( tiles );

        // creates new shrine tile
        Shrine shrine = new Shrine(
                tile.GetWorldPosition( ),
                tile.GetTilePosition( ),
                SpriteType.HealthShrine_01,
                TileType.Shrine,
                ShrineType.healing,
                Util.GetRandomInteger( 10, 25 ) );

        // add to tiles
        World.instance.AddToTiles( shrine );

        // removes the old tile
        tile.Remove( );

        Tile[] modTiles = new Tile[]{ shrine, tile };

        return modTiles;
    }

    private void createPlayer( Tile spawnTile )
    {
        if ( ActorManager.GetPlayerInstance( ) == null ) { ActorManager.CreatePlayerInstance( spawnTile ); }
        else { ActorManager.ForceMoveActor( spawnTile, ActorManager.GetPlayerInstance( ) ); }
    }

    private void initiation( )
    {

        if ( instance != null )
        {
            System.out.println( "WORLD ALREADY EXISTS!" );
            new Exception( ).printStackTrace( );
            System.exit( 1 );
        }

        // set instance
        World.instance = this;

        // instantiate lists
        this.rooms = new ArrayList< Room >( );
        this.tiles = new ArrayList< Tile >( );

        // set state
        Game.instance.setGameState( EGameState.LOADING );
    }

    private Tile CreatePredefinedMap( char[][] map )
    {

        int offsetY = 0;
        int offsetX = 0;

        Tile spawnTile = null;

        for ( int y = 0; y < map.length; y++ )
        {
            for ( int x = 0; x < map[ y ].length; x++ )
            {

                Tile tile = null;

                Coordinate tilePos = new Coordinate( x, y );
                Coordinate worldPos = new Coordinate( x * Game.SPRITE_SIZE + offsetX, y * Game.SPRITE_SIZE + offsetY );

                // read map and create a tile
                char mapChar = map[ y ][ x ];

                // first creates tiles
                switch ( mapChar )
                {
                    case '#':
                        tile = new Tile( worldPos, tilePos, SpriteType.Wall01, TileType.OuterWall ); break;
                    case '.':
                        tile = new Tile( worldPos, tilePos, SpriteType.Floor01, TileType.Floor ); break;
                    case '@':
                        tile = new Tile( worldPos, tilePos, SpriteType.SpawnTile01, TileType.Floor ); spawnTile = tile; break;
                    case 'W':
                        tile = new Tile( worldPos, tilePos, SpriteType.Wall01, TileType.Wall ); break;
                    case 'd':
                        tile = new Door( worldPos, tilePos, SpriteType.Door01, TileType.Door, false, DoorType.Normal ); break;
                    case 'L':
                        tile = new Door( worldPos, tilePos, SpriteType.LockedDoor01, TileType.LockedDoor, true, DoorType.Normal ); break;
                    case 'P':
                        tile = new Portal( worldPos, tilePos, SpriteType.Portal02, TileType.Portal, false ); break;
                    case 'E':
                        tile = new Portal( worldPos, tilePos, SpriteType.Portal02, TileType.Portal, true ); break;
                    case 'D':
                        tile = new Door( worldPos, tilePos, SpriteType.LockedDoorDiamond01, TileType.LockedDoor, true, DoorType.Diamond ); break;
                    case 'c':
                        tile = new Tile( worldPos, tilePos, SpriteType.Floor01, TileType.Floor ); break;
                    case 'l':
                        tile = new Tile( worldPos, tilePos, SpriteType.Floor01, TileType.Floor ); break;
                    case 'B':
                        tile = new Tile( worldPos, tilePos, SpriteType.TreasuryFloor01, TileType.Floor ); break;
                    default:
                        System.out.println( "INVALID CHARACTER AT CreatePredefinedMap." ); new Exception( ).printStackTrace( ); System.exit( 1 ); break;
                }

                // secondly creates items on top of tiles
                switch ( mapChar )
                {
                    case 'c':
                        ItemCreator.CreateChest( tile, false ); break;
                    case 'l':
                        ItemCreator.CreateChest( tile, true ); break;
                    case 'B': /* TODO: CREATE BANK! */
                        break;
                    default:
                        break;
                }

                // add tile to tiles list.
                tiles.add( tile );

                // increment offset
                offsetX += Game.TILE_GAP;
            }

            // increment and reset offsets.
            offsetX = 0;
            offsetY += Game.TILE_GAP;
        }
        return spawnTile;
    }

    public Tile GetTileAtPosition( Coordinate pos ) { return GetTileAtPosition( pos.getX( ), pos.getY( ) ); }

    public Tile GetTileAtPosition( int x, int y )
    {
        Tile retTile = null;
        for ( Tile tile : tiles )
        {
            Coordinate position = tile.GetTilePosition( );
            if ( position.getX( ) == x && position.getY( ) == y )
            {
                retTile = tile;
                break;
            }
        }
        return retTile;
    }

    public List< Tile > GetTilesInCardinalDirection( int posx, int posy )
    {

        List< Tile > foundTiles = new ArrayList< Tile >( );
        Tile current = null;

        for ( int y = - 1; y < 2; y++ )
        {
            for ( int x = - 1; x < 2; x++ )
            {

                if ( ( x == - 1 || x == 1 ) && y == 0 )
                {

                    // # # #
                    // 1 @ 2
                    // # # #
                    current = GetTileAtPosition( posx + x, posy + y );
                    if ( current == null ) continue;
                    foundTiles.add( current );

                }
                else if ( ( y == - 1 || y == 1 ) && x == 0 )
                {

                    // # 1 #
                    // # @ #
                    // # 2 #
                    current = GetTileAtPosition( posx + x, posy + y );
                    if ( current == null ) continue;
                    foundTiles.add( current );

                }
            }
        }
        return foundTiles;
    }

    public Tile GetTileWithWorldPosition( Coordinate wpos )
    {
        Tile ret = null;
        for ( Tile tile : tiles )
        {
            if ( tile.GetBounds( ).contains( new Point( wpos.getX( ), wpos.getY( ) ) ) )
            {
                ret = tile;
                break;
            }
        }
        return ret;
    }

    public List< Tile > GetSurroundingTiles( Coordinate pos ) { return GetSurroundingTiles( pos.getX( ), pos.getY( ) ); }

    public List< Tile > GetSurroundingTiles( int posx, int posy )
    {
        List< Tile > foundTiles = new ArrayList< Tile >( );
        for ( int y = - 1; y < 2; y++ )
        {
            for ( int x = - 1; x < 2; x++ )
            {
                foundTiles.add( GetTileAtPosition( posx + x, posy + y ) );
            }
        }
        return foundTiles;
    }

    public Tile GetTileFromDirection( Coordinate pos, Direction dir ) { return GetTileFromDirection( pos.getX( ), pos.getY( ), dir ); }

    public Tile GetTileFromDirection( int x, int y, Direction dir )
    {

        Tile tile = null;

        switch ( dir )
        {
            case North:
                tile = GetTileAtPosition( x, y - 1 );
                break;
            case South:
                tile = GetTileAtPosition( x, y + 1 );
                break;
            case West:
                tile = GetTileAtPosition( x - 1, y );
                break;
            case East:
                tile = GetTileAtPosition( x + 1, y );
                break;
            default:
                System.out.println( "GetTileFromDirection: NOT A CARDINAL DIRECTION!" );
                break;
        }
        return tile;
    }

    public Tile GetTileFromDirection( Tile current, Direction dir )
    {

        Tile tile = null;

        int x = current.GetTilePosition( ).getX( );
        int y = current.GetTilePosition( ).getY( );

        switch ( dir )
        {
            case North:
                tile = GetTileAtPosition( x, y - 1 );
                break;
            case South:
                tile = GetTileAtPosition( x, y + 1 );
                break;
            case West:
                tile = GetTileAtPosition( x - 1, y );
                break;
            case East:
                tile = GetTileAtPosition( x + 1, y );
                break;
            default:
                System.out.println( "GetTileFromDirection: NOT A CARDINAL DIRECTION!" );
                break;
        }
        return tile;
    }

    public int[] GetFreePosition( List< Tile > tiles_ )
    {

        List< Tile > possibleTiles = new ArrayList< Tile >( );
        int[] position = new int[ 4 ];

        // get all possible tiles
        for ( Tile tile : tiles_ )
        {
            if ( Util.isTileValid( tile ) ) possibleTiles.add( tile );
        }

        if ( possibleTiles.isEmpty( ) )
        {
            System.out.println( "possibleTiles is empty (World.GetFreePosition())." );
            new Exception( ).printStackTrace( );
            System.exit( 1 );
        }

        // get a random number
        int random = Util.GetRandomInteger( 0, possibleTiles.size( ) );

        // get a random tile from possible tiles.
        Tile randomTile = possibleTiles.get( random );

        // get the position of random tile.
        Coordinate randTilePos = randomTile.GetTilePosition( );

        // world-x
        position[ 0 ] = randomTile.GetWorldPosition( ).getX( );

        // world-y
        position[ 1 ] = randomTile.GetWorldPosition( ).getY( );

        // tile-x
        position[ 2 ] = randTilePos.getX( );

        // tile-y
        position[ 3 ] = randTilePos.getY( );

        return position;
    }

    public int[] GetFreePosition( )
    {
        int[] position = new int[ 4 ];

        Tile randomTile = Util.getRandomTileFromRandomRoom( rooms );

        // get the position of random tile.
        Coordinate randTilePos = randomTile.GetTilePosition( );

        // world-x
        position[ 0 ] = randomTile.GetWorldPosition( ).getX( );

        // world-y
        position[ 1 ] = randomTile.GetWorldPosition( ).getY( );

        // tile-x
        position[ 2 ] = randTilePos.getX( );

        // tile-y
        position[ 3 ] = randTilePos.getY( );

        return position;
    }

    public Coordinate ConvertTilePositionToWorld( Coordinate pos )
    {
        return new Coordinate( pos.getX( ) * Game.SPRITE_SIZE + Game.TILE_GAP * pos.getX( ),
                               pos.getY( ) * Game.SPRITE_SIZE + Game.TILE_GAP * pos.getY( ) );
    }

    // creates a new TILE and destroys the old one.
    public Tile ReplaceTile( Tile old, TileType newType, SpriteType newSprite )
    {

        Tile newTile = null;

        // create new tile
        if ( newType == TileType.Trap )
        {

            // randomize trap type
            TrapType randTrapType = TrapType.values( )[ Util.GetRandomInteger( 0, TrapType.values( ).length ) ];

            // create damage map
            Map< DamageType, Integer > dmg = new LinkedHashMap< DamageType, Integer >( );

            switch ( randTrapType )
            {
                case Projectile:
                    dmg.put( DamageType.Physical, 10 );
                    newTile = new Trap( old, SpriteType.TrapTile01, TileType.Trap, TrapType.Projectile, dmg );
                    break;
                case Gas:
                    dmg.put( DamageType.Physical, 10 );
                    newTile = new Trap( old, SpriteType.TrapTile01, TileType.Trap, TrapType.Gas, dmg );
                    break;
            }

        }
        else if ( newType == TileType.Door )
        {
            newTile = new Door( old.GetWorldPosition( ), old.GetTilePosition( ), SpriteType.Door01, TileType.Door, false, DoorType.Normal );
        }
        else if ( newType == TileType.LockedDoor )
        {
            newTile = new Door( old.GetWorldPosition( ), old.GetTilePosition( ), SpriteType.LockedDoor01, TileType.Door, true, DoorType.Normal );
        }
        else if ( newType == TileType.Floor )
        {
            newTile = new Tile( old.GetWorldPosition( ), old.GetTilePosition( ), SpriteType.Floor01, TileType.Floor );
        }
        else
        {
            System.out.println( "WORLD.REPLACETILE: TILETYPE NOT YET IMPLEMENTED!" );
            new Exception( ).printStackTrace( );
            System.exit( 1 );
        }

        // add new tile to our list of tiles.
        AddToTiles( newTile );

        // remove old tile
        old.Remove( );

        return newTile;
    }

    // Changes tiletype and spritetype of OLD tile.
    // Doesnt remove tiles.
    public void ChangeTile( Tile oldTile, TileType newTileType, SpriteType newSpriteType )
    {

        // 1. change the data of old tile.
        oldTile.SetTileType( newTileType );

        // 2. change the graphics of old tile.
        oldTile.SetSprite( SpriteCreator.instance.CreateSprite( newSpriteType ) );
    }

    public List< Tile > GetTilesOfType( TileType type )
    {
        List< Tile > foundTiles = new ArrayList< Tile >( );
        for ( int y = 0; y < worldHeight; y++ )
        {
            for ( int x = 0; x < worldWidth; x++ )
            {
                Tile current = GetTileAtPosition( x, y );
                if ( current.GetTileType( ) == type )
                {
                    foundTiles.add( current );
                }
            }
        }
        return foundTiles;
    }

    public List< Tile > GetTilesOfType( TileType type, List< Tile > tiles )
    {
        List< Tile > foundTiles = new ArrayList< Tile >( );
        for ( Tile tile : tiles )
        {
            if ( tile.GetTileType( ) == type )
            {
                foundTiles.add( tile );
            }
        }
        return foundTiles;
    }

    public int distanceBetweenTiles( Tile start, Tile goal )
    {
        int dist_x = Math.abs( start.GetTilePosition( ).getX( ) - goal.GetTilePosition( ).getX( ) );
        int dist_y = Math.abs( start.GetTilePosition( ).getY( ) - goal.GetTilePosition( ).getY( ) );
        int dist = dist_x + dist_y;
        return dist;
    }

    // works only with cardinal tiles!
    public Direction GetDirectionOfTileFromPoint( Tile from, Tile to )
    {

        Direction dir = null;

        int fromX = from.GetTilePosition( ).getX( );
        int fromY = from.GetTilePosition( ).getY( );
        int toX = to.GetTilePosition( ).getX( );
        int toY = to.GetTilePosition( ).getY( );

        if ( fromX < toX ) { dir = Direction.East; }
        else if ( fromX > toX ) { dir = Direction.West; }
        else if ( fromY < toY ) { dir = Direction.South; }
        else if ( fromY > toY ) dir = Direction.North;

        return dir;
    }

    public void Remove( )
    {
        List< Tile > temp = new ArrayList< Tile >( tiles );
        for ( Tile tile : temp ) { tile.Remove( ); }
        World.instance = null;
    }

    public List< Tile > GetTilesInCardinalDirection( Tile tile ) { return GetTilesInCardinalDirection( tile.GetTilePosition( ).getX( ), tile.GetTilePosition( ).getY( ) ); }

    public List< Tile > GetTilesInCardinalDirection( Coordinate pos ) { return GetTilesInCardinalDirection( pos.getX( ), pos.getY( ) ); }

    public void addToRooms( Room room ) { this.rooms.add( room ); }

    public void addAllToRooms( List< Room > rooms ) { this.rooms.addAll( rooms ); }

    public void AddToTiles( Tile t ) { this.tiles.add( t ); }

    public void RemoveTiles( Tile t ) { if ( tiles.contains( t ) ) this.tiles.remove( t ); }

    public WorldType getWorldType( ) { return this.worldType; }

    public List< Tile > GetTiles( ) { return this.tiles; }

    public World GetWorld( ) { return this; }

    public int GetHeight( ) { return this.worldHeight; }

    public int GetWidth( ) { return this.worldWidth; }
}
