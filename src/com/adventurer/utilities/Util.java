package com.adventurer.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.adventurer.data.Coordinate;
import com.adventurer.data.Room;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.RoomType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;
import com.adventurer.gameobjects.Chest;
import com.adventurer.gameobjects.Door;
import com.adventurer.gameobjects.Item;
import com.adventurer.gameobjects.Player;
import com.adventurer.gameobjects.Portal;
import com.adventurer.gameobjects.Tile;
import com.adventurer.main.ActorManager;
import com.adventurer.main.Game;

public class Util
{

    // ------------------------ RANDOMIZATION -------------------------

    public static Direction GetRandomCardinalDirection( ) { return Direction.values( )[ Util.GetRandomInteger( 0, 4 ) ]; }

    public static int GetRandomInteger( ) { return ThreadLocalRandom.current( ).nextInt( 0, 101 ); }

    public static int GetRandomInteger( int min, int max ) { return ThreadLocalRandom.current( ).nextInt( min, max ); }

    // ------------------------ MATH ----------------------------------

    // http://stackoverflow.com/questions/16656651/does-java-have-a-clamp-function
    public static int clamp( int val, int min, int max ) { return Math.max( min, Math.min( max, val ) ); }

    // http://www.java-gaming.org/index.php?topic=34706.0
    public static float lerp( float point1, float point2, float alpha ) { return point1 + alpha * ( point2 - point1 ); }

    // http://www.java-gaming.org/index.php?topic=34706.0
    public static int lerp( int point1, int point2, float alpha ) { return Math.round( point1 + alpha * ( point2 - point1 ) ); }

    // ----------------------- IMAGE UTILS ----------------------------

    // http://stackoverflow.com/questions/4248104/applying-a-tint-to-an-image-in-java
    public static BufferedImage tint( BufferedImage image, boolean darker )
    {

        // copy the image
        BufferedImage tintedImage = Util.deepCopy( image );

        // loop through all pixels
        for ( int x = 0; x < tintedImage.getWidth( ); x++ )
        {
            for ( int y = 0; y < tintedImage.getHeight( ); y++ )
            {

                // second parameter is if there is alpha channel.
                Color color = new Color( tintedImage.getRGB( x, y ), true );

                Color tintedColor = null;

                // make the pixel's color darker.
                if ( darker ) { tintedColor = color.darker( ).darker( ).darker( ); }
                else { tintedColor = color.brighter( ); }

                // apply color to new image.
                tintedImage.setRGB( x, y, tintedColor.getRGB( ) );
            }
        }
        return tintedImage;
    }

    // https://stackoverflow.com/questions/4248104/applying-a-tint-to-an-image-in-java
    public static BufferedImage tintWithColor( BufferedImage image, Color tintColor )
    {

        // copy the image
        BufferedImage tintedImage = Util.deepCopy( image );

        // loop through all pixels
        for ( int x = 0; x < tintedImage.getWidth( ); x++ )
        {
            for ( int y = 0; y < tintedImage.getHeight( ); y++ )
            {

                // second parameter is if there is alpha channel.
                Color pixelColor = new Color( tintedImage.getRGB( x, y ), true );

                int r = ( pixelColor.getRed( ) + tintColor.getRed( ) ) / 2;
                int g = ( pixelColor.getGreen( ) + tintColor.getGreen( ) ) / 2;
                int b = ( pixelColor.getBlue( ) + tintColor.getBlue( ) ) / 2;
                int a = pixelColor.getAlpha( );

                int rgba = ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;

                // apply color to new image.
                tintedImage.setRGB( x, y, rgba );
            }
        }
        return tintedImage;
    }

    public static BufferedImage highlightTileBorders( BufferedImage image, Color tintColor )
    {

        // copy the image
        BufferedImage tintedImage = Util.deepCopy( image );

        // loop through all pixels
        for ( int x = 0; x < tintedImage.getWidth( ); x++ )
        {
            for ( int y = 0; y < tintedImage.getHeight( ); y++ )
            {

                // get pixel + alpha values
                Color pixelColor = new Color( tintedImage.getRGB( x, y ), true );
                int r, g, b, a, rgba;


                // don't highlight the corner pixels
                if ( ( x == 0 && y == 0 ) || ( x == tintedImage.getWidth( ) - 1 && y == 0 ) ||
                        ( x == 0 && y == tintedImage.getHeight( ) - 1 ) || ( x == tintedImage.getWidth( ) - 1 && y == tintedImage.getHeight( ) - 1 ) )
                {

                    // use default pixel color.
                    r = pixelColor.getRed( );
                    g = pixelColor.getGreen( );
                    b = pixelColor.getBlue( );
                    a = pixelColor.getAlpha( );
                    rgba = ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;

                    tintedImage.setRGB( x, y, rgba );

                    continue;
                }

                // if the pixel is not a corner pixel
                if ( x == 0 || x == tintedImage.getWidth( ) - 1 || y == 0 || y == tintedImage.getHeight( ) - 1 )
                {

                    // tint the border with some color
                    // calculates average values of all color channels.
                    r = ( pixelColor.getRed( ) + tintColor.getRed( ) ) / 2;
                    g = ( pixelColor.getGreen( ) + tintColor.getGreen( ) ) / 2;
                    b = ( pixelColor.getBlue( ) + tintColor.getBlue( ) ) / 2;
                    a = 255;
                    rgba = ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;

                    // ALPHA	RED		 GREEN    BLUE
                    // 11111111 00000000 00000000 00000000 32-bit integer

                }
                else
                {

                    // use default pixel color.
                    r = pixelColor.getRed( );
                    g = pixelColor.getGreen( );
                    b = pixelColor.getBlue( );
                    a = pixelColor.getAlpha( );
                    rgba = ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;

                }

                // apply color to new image.
                tintedImage.setRGB( x, y, rgba );
            }
        }
        return tintedImage;
    }

    // http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    public static BufferedImage deepCopy( BufferedImage bi )
    {
        ColorModel cm = bi.getColorModel( );
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied( );
        WritableRaster raster = bi.copyData( null );
        return new BufferedImage( cm, raster, isAlphaPremultiplied, null );
    }

    // ------------------ RESOURCE LOADING -----------------------

    // https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
    public static void loadCustomFont( )
    {

        String fullPath = "resources/fonts/" + Game.CUSTOMFONTFOLDER + "/" + Game.CUSTOMFONTNAME + Game.CUSTOMFONTEXTENSION;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment( );
        Font font = null;

        try
        {
            font = Font.createFont( Font.TRUETYPE_FONT, new File( fullPath ) );
            ge.registerFont( font );
        }
        catch ( FontFormatException | IOException e ) { e.printStackTrace( ); }

        // cache the font
        Game.instance.setCustomFont( font );

        System.out.println( "Succesfully loaded custom font: " + font.getFontName( ) );
    }

    // ----------------- OTHER UTILS -------------------------------

    public static String colorToString( Color color ) { return color.getRed( ) + "," + color.getGreen( ) + "," + color.getBlue( ); }

    public static String generateRichTextForColor( Color color, String string ) { return "<color=\"" + Util.colorToString( color ) + "\">" + string + "</color>"; }

    public static String generateRichTextForColor( Color color, int amount ) { return generateRichTextForColor( color, amount + "" ); }

    public static String Capitalize( String s )
    {
        String a = s.substring( 0, 1 ).toUpperCase( );
        String b = s.substring( 1, s.length( ) ).toLowerCase( );
        return a + b;
    }

    public static Coordinate calculateCameraPos( Player player )
    {
        int x = ( ( - player.GetWorldPosition( ).getX( ) * Game.CAMERAZOOM ) - ( Game.SPRITESIZE - Game.WIDTH / 2 ) ) / Game.CAMERAZOOM;
        int y = ( ( - player.GetWorldPosition( ).getY( ) * Game.CAMERAZOOM ) - ( Game.SPRITESIZE - Game.HEIGHT / 2 ) ) / Game.CAMERAZOOM;
        return new Coordinate( x, y );
    }

    public static String getRandomSubtitle( )
    {

        String[] adjectives = new String[]{
                "Awesome", "Good", "Amazing", "Epic", "Wonderful",
                "Acceptable", "Excellent", "Great", "Satisfactory",
                "Superb", "Exceptional", "Marvelous", "Spanking", "Rad",
                "Ace", "Cracking" };
        String[] substantives = new String[]{
                "Game", "Dungeoncrawler", "Roguelike",
                "Torture", "Misery", "Torment", "Ache",
                "Suffering", "Despair", "Sorrow", "Adventure",
                "Play", "Distraction", "Joke", "Amusement" };

        return adjectives[ Util.GetRandomInteger( 0, adjectives.length - 1 ) ] + " " + substantives[ Util.GetRandomInteger( 0, substantives.length - 1 ) ] + "!";
    }

    // ------------------------ STAT CALCULATIONS ---------------------

    public static int calcHealth( int vit )
    {
        return Game.PLAYER_START_BASE_HEALTH + ( vit * Game.VITALITY_TO_HEALTH_MULTIPLIER );
    }

    public static int calcMana( int intelligence )
    {
        return Game.PLAYER_START_BASE_MANA + ( intelligence * Game.INTELLIGENCE_TO_MANA_MULTIPLIER );
    }

    public static int calcHealth( )
    {
        Player player = ActorManager.GetPlayerInstance( );
        return Game.PLAYER_START_BASE_HEALTH + ( player.getStats( ).getSumVit( ) * Game.VITALITY_TO_HEALTH_MULTIPLIER );
    }

    public static int calcMana( )
    {
        Player player = ActorManager.GetPlayerInstance( );
        return Game.PLAYER_START_BASE_MANA + ( player.getStats( ).getSumInt( ) * Game.INTELLIGENCE_TO_MANA_MULTIPLIER );
    }

    public static int calcMeleeDamage( int a )
    {
        return a * Game.STRENGTH_TO_MELEE_DAMAGE_MULTIPLIER;
    }

    public static int calcMeleeDamage( )
    {
        Player player = ActorManager.GetPlayerInstance( );
        return player.getStats( ).getSumStr( ) * Game.STRENGTH_TO_MELEE_DAMAGE_MULTIPLIER;
    }

    public static int calcRangedDamage( int a )
    {
        return a * Game.DEXTERITY_TO_RANGED_DAMAGE_MULTIPLIER;
    }

    public static int calcRangedDamage( )
    {
        Player player = ActorManager.GetPlayerInstance( );
        return player.getStats( ).getSumDex( ) * Game.DEXTERITY_TO_RANGED_DAMAGE_MULTIPLIER;
    }

    public static int calcMagicDamage( int a )
    {
        return a * Game.INTELLIGENCE_TO_MAGIC_DAMAGE_MULTIPLIER;
    }

    public static int calcMagicDamage( )
    {
        Player player = ActorManager.GetPlayerInstance( );
        return player.getStats( ).getSumInt( ) * Game.INTELLIGENCE_TO_MAGIC_DAMAGE_MULTIPLIER;
    }

    // ------------------------ DUNGEON/TILE FUNCTIONS ---------------------

    public static Tile getRandomTileFromRandomRoom( List< Room > rooms_ )
    {
        List< Tile > tiles_ = new ArrayList< Tile >( );
        for ( Tile t : rooms_.get( Util.GetRandomInteger( 0, rooms_.size( ) ) ).getTiles( ) ) { tiles_.add( t ); }
        return getRandomTile( tiles_ );
    }

    public static Tile getRandomTile( List< Tile > tiles_ )
    {

        List< Tile > candidates = new ArrayList< Tile >( );
        Tile chosen = null;

        for ( Tile t : tiles_ ) { if ( isTileValid( t ) ) candidates.add( t ); }

        // if we dont have any candidates...
        if ( candidates.size( ) == 0 ) return null;

        chosen = candidates.get( Util.GetRandomInteger( 0, candidates.size( ) ) );
        return chosen;
    }

    public static boolean isTileWalkable( TileType type )
    {
        if ( type == TileType.Floor || type == TileType.Trap || type == TileType.Shrine ) { return true; }
        else { return false; }
    }

    public static boolean doesTileHaveChest( Tile tile )
    {
        if ( tile.GetItems( ).isEmpty( ) ) return false;
        for ( Item item : tile.GetItems( ) ) { if ( item instanceof Chest ) return true; }
        return false;
    }

    public static Chest getChest( Tile tile )
    {
        Chest chest = null;
        for ( Item item : tile.GetItems( ) )
        {
            if ( item instanceof Chest )
            {
                chest = ( Chest ) item;
                break;
            }
        }
        return chest;
    }

    public static boolean isTileValid( Tile tile )
    {
        if ( tile.isWalkable( ) && tile.GetActor( ) == null && doesTileHaveChest( tile ) == false ) { return true; }
        else { return false; }
    }

    public static Tile getTileAt( Coordinate pos, List< Tile > tiles )
    {
        Tile tile = null;
        for ( Tile t : tiles )
        {
            if ( t.GetTilePosition( ).getX( ) == pos.getX( ) && t.GetTilePosition( ).getY( ) == pos.getY( ) )
            {
                tile = t;
                break;
            }
        }
        return tile;
    }

    public static List< Tile > getNeighboringTiles( Tile tile, List< Tile > tiles )
    {
        List< Tile > neighbors = new ArrayList< Tile >( );

        int x = tile.GetTilePosition( ).getX( );
        int y = tile.GetTilePosition( ).getY( );

        // neighbors
        Coordinate top = new Coordinate( x, y - 1 );
        Coordinate bottom = new Coordinate( x, y + 1 );
        Coordinate left = new Coordinate( x - 1, y );
        Coordinate right = new Coordinate( x + 1, y );

        for ( Tile t : tiles )
        {

            int x_ = t.GetTilePosition( ).getX( );
            int y_ = t.GetTilePosition( ).getY( );

            // check if this tile is a neighbor of the current tile.
            // tiles contains only tile type of error tiles.

            if (
                    ( top.getX( ) == x_ && top.getY( ) == y_ ) ||
                            ( bottom.getX( ) == x_ && bottom.getY( ) == y_ ) ||
                            ( left.getX( ) == x_ && left.getY( ) == y_ ) ||
                            ( right.getX( ) == x_ && right.getY( ) == y_ )
            ) { neighbors.add( t ); }

        }
        return neighbors;
    }

    public static Tile replaceTile( Tile old, TileType newType, SpriteType newSpriteType )
    {
        Tile tile_ = new Tile( old.GetWorldPosition( ), old.GetTilePosition( ), newSpriteType, newType );
        old.Remove( );
        return tile_;
    }

    public static Door replaceTileWithDoor( Tile old, boolean locked )
    {
        TileType type_ = TileType.Door;
        if ( locked ) type_ = TileType.LockedDoor;
        Door door = new Door( old.GetWorldPosition( ), old.GetTilePosition( ), SpriteType.Door01, type_, locked, DoorType.Normal );
        old.Remove( );
        return door;
    }

    public static Portal replaceTileWithPortal( Tile old, boolean isExit )
    {
        Portal portal = new Portal( old.GetWorldPosition( ), old.GetTilePosition( ), SpriteType.Portal02, TileType.Portal, isExit );
        old.Remove( );
        return portal;
    }

    public static Tile getRandomNeighboringTile( Tile tile, List< Tile > tiles )
    {
        Tile chosen = null;

        Direction[] ds = new Direction[]{ Direction.North, Direction.East, Direction.South, Direction.West };
        List< Direction > dds = new ArrayList< Direction >( );
        for ( Direction d : ds )
        {
            dds.add( d );
        }

        // randomize the search order.
        Collections.shuffle( dds );

        // get first neighboring tile.
        for ( Direction d : dds )
        {
            Tile n = getNeighboringTile( tile, d, tiles );
            if ( n != null ) chosen = n;
        }
        return chosen;
    }

    public static Tile getNeighboringTile( Tile tile, Direction dir, List< Tile > tiles )
    {
        Tile chosen = null;

        int x = tile.GetTilePosition( ).getX( );
        int y = tile.GetTilePosition( ).getY( );

        switch ( dir )
        {
            case East:
                chosen = getTileAt( new Coordinate( x + 1, y ), tiles );
                break;
            case North:
                chosen = getTileAt( new Coordinate( x, y - 1 ), tiles );
                break;
            case South:
                chosen = getTileAt( new Coordinate( x, y + 1 ), tiles );
                break;
            case West:
                chosen = getTileAt( new Coordinate( x - 1, y ), tiles );
                break;
        }
        return chosen;
    }

    public static Tile getRandomNeighborWall( Tile tile, List< Tile > tiles, List< Tile > concreteWalls )
    {
        List< Tile > ts = new ArrayList< Tile >( );
        for ( Tile t : getNeighboringTiles( tile, tiles ) ) { if ( concreteWalls.contains( t ) ) ts.add( t ); }
        if ( ts.isEmpty( ) ) { return null; }
        else { return ts.get( Util.GetRandomInteger( 0, ts.size( ) ) ); }
    }

    public static Tile getRandomNeighborOfType( Tile tile, TileType type, List< Tile > tiles )
    {
        List< Tile > ts = new ArrayList< Tile >( );
        for ( Tile t : getNeighboringTiles( tile, tiles ) ) { if ( t.GetTileType( ) == type ) ts.add( t ); }
        return ts.get( Util.GetRandomInteger( 0, ts.size( ) ) );
    }

    // replaces all error tiles with walls.
    public static List< Tile > replaceAllErrorTiles( List< Tile > tiles )
    {
        List< Tile > tiles_ = new ArrayList< Tile >( tiles );
        for ( Tile t : tiles )
        {
            if ( t.GetTileType( ) == TileType.Error )
            {
                tiles_.remove( t );
                Tile tile_ = Util.replaceTile( t, TileType.Wall, SpriteType.Wall01 );
                tiles_.add( tile_ );
            }
        }
        return tiles_;
    }

    public static RoomType getRandomRoomTypeNotIn( List< RoomType > types )
    {

        RoomType rtype = null;

        // 1. create a list of types
        // 2. shuffle that list
        List< RoomType > randomizedTypes = new ArrayList< RoomType >( );
        for ( int i = 0; i < RoomType.values( ).length; i++ ) { randomizedTypes.add( RoomType.values( )[ i ] ); }
        Collections.shuffle( randomizedTypes );

        // loop through that list
        // and get first item that is not in
        // types list.
        for ( RoomType type : randomizedTypes )
        {
            if ( types.contains( type ) == false )
            {
                rtype = type;
                break;
            }
        }
        return rtype;
    }
}
