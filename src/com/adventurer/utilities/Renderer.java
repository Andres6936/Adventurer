package com.adventurer.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adventurer.data.Camera;
import com.adventurer.data.Coordinate;
import com.adventurer.data.ParseData;
import com.adventurer.data.World;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.GameState;
import com.adventurer.enumerations.WorldType;
import com.adventurer.gameobjects.Player;
import com.adventurer.main.ActorManager;
import com.adventurer.main.Game;
import com.adventurer.main.Handler;

public class Renderer
{

    public static void renderMainMenu( Graphics g )
    {

        Graphics2D g2d = ( Graphics2D ) g;

        // get background image..
        if ( Game.instance.getBackgroundImage( ) == null )
        {
            try
            {
                Game.instance.setBackgroundImage( ImageIO.read( Renderer.class.getClass( ).getResourceAsStream( "/" + Game.BACKGROUNDNAME ) ) );
            }
            catch ( IOException e ) { e.printStackTrace( ); }
        }

        // render background image
        Renderer.FillScreenWithImage( g, Game.instance.getBackgroundImage( ) );

        // the position of all GUI elements
        // --> cleaner look.
        int xPos = Game.WIDTH / 5 - 20;

        // title
        Renderer.renderString( "ADVENTURER", new Coordinate( xPos, 125 ), Color.white, 36, g2d );

        // game subtitle :)
        Renderer.renderString( Game.instance.getMainmenuSubtitle( ), new Coordinate( xPos + 20, 170 ), Color.gray, 18, g2d );

        // version info
        Renderer.renderString( "Version: extra early\nDate: 13.8.2017", new Coordinate( xPos, 550 ), Color.gray, 16, g2d );

        // creator info
        Renderer.renderString( "By Baserfaz (Heikki Heiskanen)", new Coordinate( 900, 660 ), Color.gray, 16, g2d );

        // draw play button
        Renderer.renderButton( "Play", new Coordinate( xPos, 250 ), new Coordinate( 200, 50 ), Color.black, Color.white, 21, true, g2d );

        // draw exit button
        Renderer.renderButton( "Exit", new Coordinate( xPos, 350 ), new Coordinate( 200, 50 ), Color.black, Color.white, 21, true, g2d );
    }

    public static void renderLoading( Graphics g )
    {

        // get game state
        GameState gameState = Game.instance.getGameState( );

        Graphics2D g2d = ( Graphics2D ) g;

        // set background
        Renderer.FillScreen( g, Color.black );

        // title
        Renderer.renderString( "Adventurer", new Coordinate( Game.WIDTH / 3, 100 ), Color.white, 24, g2d );

        // information about the dungeon
        if ( World.instance.getWorldType( ) == WorldType.Random )
        {

            // dungeon generation states
            Renderer.renderString( ">> Generating " + DungeonGeneration.state,
                                   new Coordinate( Game.WIDTH / 3, 300 ), Color.white, 16, g2d );

            // dungeon settings
            Renderer.renderString( "Dungeon size: " + Game.WORLDWIDTH + "x" + Game.WORLDHEIGHT +
                                           "\nMax room count: " + Game.ROOM_COUNT +
                                           "\nMax doors per room: " + Game.ROOM_DOOR_MAX_COUNT,
                                   new Coordinate( Game.WIDTH / 3, 350 ), Color.white, 16, g2d );

        }
        else if ( World.instance.getWorldType( ) == WorldType.Predefined )
        {

            if ( gameState == GameState.Loading )
            {
                Renderer.renderString( "Creating world...", new Coordinate( Game.WIDTH / 3, 300 ), Color.white, 16, g2d );
            }
            else if ( gameState == GameState.Ready )
            {
                // TODO: the name of the world/predefined map should not be hard coded.
                Renderer.renderString( "World created. \n>> Lobby", new Coordinate( Game.WIDTH / 3, 300 ), Color.white, 16, g2d );
            }

        }

        // print finished
        if ( gameState == GameState.Ready )
        {
            Renderer.renderString( "Press any key to continue...",
                                   new Coordinate( Game.WIDTH / 3, 500 ), Color.white, 16, g2d );
        }
    }

    public static void renderInGame( Graphics g )
    {

        Graphics2D g2d = ( Graphics2D ) g;

        // set background
        Renderer.FillScreen( g, Color.black );

        // zoom
        g2d.scale( Game.CAMERA_ZOOM, Game.CAMERA_ZOOM );

        // TODO: CAMERA STUFF SHOULD NOT BE HERE!
        // camera follow
        Player player = ActorManager.GetPlayerInstance( );
        if ( player != null )
        {

            Coordinate pos = Util.calculateCameraPos( player );
            int x = pos.getX( );
            int y = pos.getY( );

            // translate
            g.translate( x, y );

            // update camera's position
            Camera.instance.Update( new Coordinate( - x, - y ), Game.CAMERA_WIDTH, Game.CAMERA_HEIGHT );

            if ( Game.DRAW_CAMERA )
            {
                g.setColor( Color.red );
                g.drawRect( ( int ) Camera.instance.getCameraBounds( ).getX( ),
                            ( int ) Camera.instance.getCameraBounds( ).getY( ),
                            ( int ) Camera.instance.getCameraBounds( ).getWidth( ),
                            ( int ) Camera.instance.getCameraBounds( ).getHeight( ) );
            }

        }
        else { Renderer.FillScreen( g, Color.black ); }

        // render objects and GUI
        Handler.instance.render( g );
    }

    // https://stackoverflow.com/questions/11367324/how-do-i-scale-a-bufferedimage
    private static BufferedImage getScaledImage( BufferedImage sprite, int w, int h )
    {
        BufferedImage resizedImg = new BufferedImage( w, h, BufferedImage.TRANSLUCENT );
        Graphics2D g2 = resizedImg.createGraphics( );
        g2.drawImage( sprite, 0, 0, w, h, null );
        g2.dispose( );
        return resizedImg;
    }

    public static void FillScreen( Graphics g, Color color )
    {
        g.setColor( color );
        g.fillRect( 0, 0, Game.WIDTH, Game.HEIGHT );
    }

    public static void FillScreenWithImage( Graphics g, Image img ) { g.drawImage( img, 0, 0, Game.WIDTH, Game.HEIGHT, null );}

    public static void RenderSpriteWithBorder( BufferedImage sprite, Coordinate pos, Graphics g, Color borderColor )
    {
        BufferedImage img = Util.highlightTileBorders( sprite, borderColor );
        g.drawImage( img, pos.getX( ), pos.getY( ), Game.SPRITE_SIZE, Game.SPRITE_SIZE, null );
    }

    public static void RenderSpriteWithTint( BufferedImage sprite, Coordinate pos, Graphics g, Color tint )
    {
        BufferedImage img = Util.tintWithColor( sprite, tint );
        g.drawImage( img, pos.getX( ), pos.getY( ), Game.SPRITE_SIZE, Game.SPRITE_SIZE, null );
    }

    // render without rotation
    public static void RenderSprite( BufferedImage sprite, Coordinate pos, Graphics g )
    {
        g.drawImage( sprite, pos.getX( ), pos.getY( ), Game.SPRITE_SIZE, Game.SPRITE_SIZE, null );
    }

    // render with 90 degree rotation
    public static void RenderSprite( BufferedImage sprite, Coordinate pos, Direction dir, Graphics g )
    {

        double angle = 0.0;
        if ( dir == Direction.East ) { angle = 180.0; }
        else if ( dir == Direction.West ) { angle = 0.0; }
        else if ( dir == Direction.South ) { angle = 270.0; }
        else if ( dir == Direction.North ) angle = 90.0;

        RenderSpriteWithRotation( sprite, pos, angle, g );
    }

    // render with free angle
    public static void RenderSpriteWithRotation( BufferedImage sprite, Coordinate pos, double angle, Graphics g )
    {

        Graphics2D g2d = ( Graphics2D ) g;

        double rot = Math.toRadians( angle );
        int x = pos.getX( );
        int y = pos.getY( );
        int halfOfSpriteSize = Game.SPRITE_SIZE / 2;
        int xcenter = x + halfOfSpriteSize;
        int ycenter = y + halfOfSpriteSize;

        g2d.rotate( rot, xcenter, ycenter );
        g.drawImage( sprite, x, y, Game.SPRITE_SIZE, Game.SPRITE_SIZE, null );
        g2d.rotate( - rot, xcenter, ycenter );
    }

    public static void renderButton( String txt, Coordinate pos,
                                     Coordinate size, Color fontCol, Color rectColor, int fontSize, boolean fill, Graphics2D g2d )
    {

        // font
        Font font = new Font( "Consolas", Font.PLAIN, fontSize );

        // font settings
        g2d.setFont( font );

        // positions
        int y = pos.getY( );
        int x = pos.getX( );
        int width = size.getX( );
        int height = size.getY( );

        // -------------------------- RENDER ------------------------

        // set color for the rectangle
        g2d.setColor( rectColor );

        // render rectangle and fill it
        g2d.drawRect( x, y, width, height );
        if ( fill ) { g2d.fillRect( x, y, width, height ); }

        // calculate text position inside the button
        Coordinate txtpos = new Coordinate( x + width / 2 - 25, y + height / 2 - 15 );

        // render string
        Renderer.renderString( txt, txtpos, fontCol, fontSize, g2d );
    }

    public static void renderRect( Coordinate pos, Coordinate size, Color borderColor, Color fillColor, boolean fill, Graphics g2d )
    {
        // positions
        int y = pos.getY( );
        int x = pos.getX( );
        int width = size.getX( );
        int height = size.getY( );

        // set color for the border
        g2d.setColor( borderColor );

        // render rectangle and fill it
        g2d.drawRect( x, y, width, height );
        if ( fill )
        {
            g2d.setColor( fillColor );
            g2d.fillRect( x, y, width, height );
        }
    }

    public static void renderString( String txt, Coordinate pos, Color baseColor, int fontSize, Graphics2D g2d )
    {

        // TODO: GUI scaling?
        int calc_fontsize = fontSize * 2 - 4;

        // create font
        Font font = new Font( Game.instance.getCustomFont( ).getFontName( ), Font.PLAIN, calc_fontsize );

        // font settings
        g2d.setFont( font );
        g2d.setColor( baseColor );

        int y = pos.getY( );
        int x = pos.getX( );

        // https://stackoverflow.com/questions/4413132/problems-with-newline-in-graphics2d-drawstring
        for ( String line : txt.split( "\n" ) )
        {

            boolean hasRichText = false;

            // calculate string height from the start position
            y += g2d.getFontMetrics( ).getHeight( ) + Game.LINEHEIGHT;

            // parse the string for color commands
            ParseData data = RichTextParser.parseStringColor( line );

            if ( data.getString( ) != null )
            {

                hasRichText = true;

                // cache vars
                int count = 0, xPos = 0;
                int[] positions = data.getPositions( );
                Color col = data.getColor( );

                for ( char c : data.getString( ).toCharArray( ) )
                {

                    // calculate x-position
                    if ( count == 0 ) { xPos = x; }
                    else { xPos += g2d.getFontMetrics( ).charWidth( c ); }

                    if ( count > positions[ 0 ] && count < positions[ 1 ] )
                    {

                        // change the color
                        g2d.setColor( col );
                        g2d.drawString( c + "", xPos, y );

                    }
                    else
                    {

                        // if the current character is not inside the rich text we use basecolor.
                        g2d.setColor( baseColor );
                        g2d.drawString( c + "", xPos, y );

                    }

                    count++;
                }
            }

            // draw the string as it is,
            // there is no richtext in it.
            if ( hasRichText == false )
            {
                g2d.setColor( baseColor );
                g2d.drawString( line, x, y );
            }

        }
    }

}
