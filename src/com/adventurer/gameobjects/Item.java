package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.data.World;
import com.adventurer.enumerations.ItemRarity;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Handler;
import com.adventurer.utilities.Renderer;
import com.adventurer.utilities.Util;

public class Item extends GameObject
{

    protected String itemName;
    protected String description;
    protected int value;
    protected ItemRarity itemRarity;

    public Item( Tile tile, SpriteType spritetype, String name, String description, int value, ItemRarity itemRarity )
    {
        super( tile.GetWorldPosition( ), tile.GetTilePosition( ), spritetype );
        this.itemName = name;
        this.description = description;
        this.value = value;
        this.itemRarity = itemRarity;
    }

    public void Remove( )
    {

        // get tile
        Tile tile = World.instance.GetTileAtPosition( this.GetTilePosition( ) );

        tile.RemoveItem( this );

        // remove this object from handler
        // -> no longer ticks
        Handler.instance.RemoveObject( this );

        // hide
        Hide( );
    }

    public void moveItemTo( Tile tile )
    {
        this.SetTilePosition( tile.GetTilePosition( ) );
        this.SetWorldPosition( tile.GetWorldPosition( ) );
        tile.AddItem( this );
    }

    public void render( Graphics g )
    {
        if ( hidden == false )
        {
            Renderer.RenderSprite( sprite, this.GetWorldPosition( ), g );
        }
        else if ( discovered == true && hidden == true )
        {
            Renderer.RenderSprite( Util.tint( sprite, true ), this.GetWorldPosition( ), g );
        }
    }

    public String getName( ) { return this.itemName; }

    public int getValue( ) { return this.value; }

    public String getDescription( ) { return this.description; }

    public ItemRarity getRarity( ) { return this.itemRarity; }

    public void tick( ) {}

    public Rectangle GetBounds( ) { return null; }
}
