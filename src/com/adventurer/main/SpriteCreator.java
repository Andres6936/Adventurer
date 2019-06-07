package com.adventurer.main;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.SpriteType;

public class SpriteCreator
{

    private String path;
    private int height;
    private int width;
    private int[] pixels;

    public static SpriteCreator instance;

    public SpriteCreator( String path )
    {

        if ( instance != null ) return;
        instance = this;

        BufferedImage image = null;

        // get the sprite sheet
        try { image = ImageIO.read( getClass( ).getResourceAsStream( "/" + path ) ); }
        catch ( IOException e ) { e.printStackTrace( ); }

        // set vars
        this.path = path;
        this.height = image.getHeight( );
        this.width = image.getWidth( );

        // load the color data
        pixels = image.getRGB( 0, 0, width, height, null, 0, width );
    }

    // https://stackoverflow.com/questions/9558981/flip-image-with-graphics2d
    public BufferedImage FlipSpriteVertically( BufferedImage img )
    {
        AffineTransform tx = AffineTransform.getScaleInstance( 1, - 1 );
        tx.translate( 0, - img.getHeight( null ) );
        AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
        BufferedImage image = op.filter( img, null );
        return image;
    }

    // https://stackoverflow.com/questions/9558981/flip-image-with-graphics2d
    public BufferedImage FlipSpriteHorizontally( BufferedImage img )
    {
        AffineTransform tx = AffineTransform.getScaleInstance( - 1, 1 );
        tx.translate( - img.getWidth( null ), 0 );
        AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
        BufferedImage retimage = op.filter( img, null );
        return retimage;
    }

    public BufferedImage CreateSprite( SpriteType type )
    {

        BufferedImage sprite = new BufferedImage( Game.SPRITESIZE, Game.SPRITESIZE, BufferedImage.TYPE_INT_ARGB );
        int[] spritePixelData = new int[ sprite.getWidth( ) * sprite.getHeight( ) ];

        // choose layout
        Coordinate pos = getSpritePosSimplified( type );

        int row = pos.getX( );
        int column = pos.getY( );

        // the tiles are 16x16
        // calculate tile's pixel locations.
        int startX = column * 16;
        int endX = startX + 16;
        int startY = row * 16;
        int endY = startY + 16;

        int currentPixel = 0;

        // get the pixel array
        for ( int y = startY; y < endY; y++ )
        {
            for ( int x = startX; x < endX; x++ )
            {
                // get the pixel data from the sprite sheet.
                spritePixelData[ currentPixel ] = pixels[ y * width + x ];
                currentPixel++;
            }
        }

        // set pixels
        for ( int y = 0; y < sprite.getHeight( ); y++ )
        {
            for ( int x = 0; x < sprite.getWidth( ); x++ )
            {
                sprite.setRGB( x, y, spritePixelData[ y * sprite.getWidth( ) + x ] );
            }
        }
        return sprite;
    }

    private Coordinate getSpritePosSimplified( SpriteType type )
    {
        int row = 0, column = 0;

        switch ( type )
        {

            // actors
            case Player:
                row = 0; column = 0; break;
            case GenericEnemy:
                row = 0; column = 1; break;

            // floor
            case Floor01:
                row = 1; column = 0; break;
            case Grass01:
                row = 1; column = 1; break;
            case Water01:
                row = 1; column = 2; break;
            case Sand01:
                row = 1; column = 3; break;
            case TreasuryFloor01:
                row = 1; column = 4; break;
            case DeepWater01:
                row = 1; column = 5; break;
            case Jungle01:
                row = 1; column = 6; break;
            case MagicFloor01:
                row = 1; column = 7; break;
            case NormalFloor01:
                row = 1; column = 8; break;
            case SpawnTile01:
                row = 1; column = 9; break;

            // walls
            case Wall01:
                row = 3; column = 0; break;
            case DestructibleWall01:
                row = 3; column = 1; break;

            // doors
            case Door01:
                row = 4; column = 0; break;
            case LockedDoor01:
                row = 5; column = 0; break;
            case LockedDoorDiamond01:
                row = 5; column = 1; break;

            // chests
            case Chest01:
                row = 6; column = 0; break;
            case Chest02:
                row = 6; column = 1; break;
            case LockedChest01:
                row = 7; column = 0; break;
            case LockedChest02:
                row = 7; column = 1; break;

            // items
            case GenericItem:
                row = 13; column = 0; break;
            case Gold01:
                row = 13; column = 1; break;
            case Key:
                row = 13; column = 2; break;
            case DiamondKey:
                row = 13; column = 3; break;
            case HealthPotion:
                row = 15; column = 0; break;
            case ManaPotion:
                row = 15; column = 1; break;

            // portals
            case Portal01:
                row = 8; column = 0; break;
            case Portal02:
                row = 8; column = 1; break;

            // shrines
            case HealthShrine_01:
                row = 12; column = 0; break;
            case UsedShrine:
                row = 12; column = 1; break;

            // bombs and traps
            case Bomb01:
                row = 9; column = 1; break;

            // projectiles
            case Spear01:
                row = 10; column = 0; break;
            case Arrow01:
                row = 10; column = 1; break;

            // other
            case Hit01:
                row = 14; column = 0; break;
            case Heal:
                row = 14; column = 2; break;
            case HealMana:
                row = 14; column = 3; break;
            case Error:
                row = 11; column = 0; break;

            default:
                System.out.println( "NO SPRITE OF TYPE: " + type ); break;
        }
        return new Coordinate( row, column );
    }

    public String GetPath( ) { return this.path; }

    public int[] GetPixelArray( ) { return this.pixels; }

    public int GetWidth( ) { return this.width; }

    public int GetHeight( ) { return this.height; }
}
