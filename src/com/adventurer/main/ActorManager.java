package com.adventurer.main;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.DamageType;
import com.adventurer.enumerations.EnemyType;
import com.adventurer.enumerations.RootElement;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.gameobjects.*;
import com.adventurer.utilities.FileReader;
import com.adventurer.utilities.Util;

public class ActorManager
{

    private static Player playerInstance = null;
    private static Enemy[] enemyInstances = null;

    public static Player CreatePlayerInstance( Tile tile )
    {

        if ( playerInstance != null )
        {
            System.out.println( "Player is already instantiated!" );
            new Exception( ).printStackTrace( );
            System.exit( 1 );
        }

        // create player and add it to our handler.
        playerInstance = new Player( tile.GetWorldPosition( ), tile.GetTilePosition( ), SpriteType.Player, null );

        return playerInstance;
    }

    public static void RemovePlayer( )
    {
        if ( playerInstance != null )
        {
            playerInstance.Remove( );
            playerInstance = null;
        }
    }

    public static void ForceMoveActor( Tile tile, Actor actor )
    {
        Handler.instance.RemoveObject( actor );
        actor.forceMove( tile.GetWorldPosition( ), tile.GetTilePosition( ) );
        Handler.instance.AddObject( actor );
    }

    public static void ForceMovePlayerToFreePosition( )
    {
        World world = World.instance.GetWorld( );

        // get position
        int[] pos = world.GetFreePosition( );

        // create coordinates
        Coordinate playerWorldPos = new Coordinate( pos[ 0 ], pos[ 1 ] );
        Coordinate playerTilePos = new Coordinate( pos[ 2 ], pos[ 3 ] );

        playerInstance.forceMove( playerWorldPos, playerTilePos );
    }

    public static void CreateEnemies( int count, List< Tile > tiles )
    {

        count = Math.min( count, Game.ROOM_MIN_HEIGHT * Game.ROOM_MIN_WIDHT );
        for ( int i = 0; i < count; i++ )
        {

            EnemyType randomType = EnemyType.values( )[ Util.GetRandomInteger( 0, EnemyType.values( ).length ) ];

            // vars
            int damage = 0, health = 0, movementSpeed = 0, movementCooldownBase = 0, exp = 0;
            String name = "";
            boolean isRanged = false;
            Map< DamageType, Integer > resistances = new LinkedHashMap< DamageType, Integer >( );

            // read enemy data
            Map< String, String > retval = FileReader.readXMLGameData( randomType.toString( ), RootElement.enemy );

            // go through the data and set stuff
            for ( Map.Entry< String, String > entry : retval.entrySet( ) )
            {
                String key = entry.getKey( ).toUpperCase( );
                String val = entry.getValue( ).toUpperCase( );

                if ( key.equals( "DAMAGE" ) ) { damage = Integer.parseInt( val ); }
                else if ( key.equals( "HEALTH" ) ) { health = Integer.parseInt( val ); }
                else if ( key.equals( "NAME" ) ) { name = Util.Capitalize( val ); }
                else if ( key.equals( "ISRANGED" ) ) { isRanged = Boolean.parseBoolean( val ); }
                else if ( key.equals( "MOVEMENTSPEED" ) ) { movementSpeed = Integer.parseInt( val ); }
                else if ( key.equals( "MOVEMENTCOOLDOWNBASE" ) ) { movementCooldownBase = Integer.parseInt( val ); }
                else if ( key.equals( "EXP" ) ) { exp = Integer.parseInt( val ); }
                else if ( key.equals( "PHYSICALRES" ) )
                {
                    resistances.put( DamageType.Physical, Integer.parseInt( val ) );
                }
                else if ( key.equals( "FIRERES" ) ) { resistances.put( DamageType.Fire, Integer.parseInt( val ) ); }
                else if ( key.equals( "FROSTRES" ) ) { resistances.put( DamageType.Frost, Integer.parseInt( val ) ); }
                else if ( key.equals( "SHOCKRES" ) ) { resistances.put( DamageType.Shock, Integer.parseInt( val ) ); }
                else if ( key.equals( "HOLYRES" ) ) resistances.put( DamageType.Holy, Integer.parseInt( val ) );
            }
            CreateEnemy( name, health, damage, randomType, tiles, isRanged, movementSpeed, resistances, movementCooldownBase, exp );
        }
    }

    public static Enemy CreateEnemy( String name, int maxHP, int damage,
                                     EnemyType enemyType, List< Tile > tiles_, boolean isRanged, int movementSpeed, Map< DamageType, Integer > resistances, int movementCooldownBase, int exp )
    {

        // get position
        int[] pos = World.instance.GetFreePosition( tiles_ );

        SpriteType spriteType = null;

        // TODO: enemy sprites
        spriteType = SpriteType.GenericEnemy;

        Coordinate enemyWorldPos = new Coordinate( pos[ 0 ], pos[ 1 ] );
        Coordinate enemyTilePos = new Coordinate( pos[ 2 ], pos[ 3 ] );

        // create enemy object
        return new Enemy( enemyWorldPos, enemyTilePos, enemyType, spriteType,
                          maxHP, 0, damage, damage, damage, name, isRanged, movementSpeed, resistances, movementCooldownBase, exp );
    }

    public static Enemy[] GetEnemyInstances( ) { return enemyInstances; }

    public static Player GetPlayerInstance( ) { return playerInstance; }
}
