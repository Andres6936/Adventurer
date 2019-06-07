package com.adventurer.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Handler;
import com.adventurer.utilities.Renderer;

public class VisualEffect extends GameObject
{

    protected long liveTimer = 0;
    protected boolean isAlive = true;

    public VisualEffect( Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int timeToLiveInMs )
    {
        super( worldPos, tilePos, spritetype );

        this.liveTimer = System.currentTimeMillis( ) + timeToLiveInMs;
    }

    public void tick( )
    {
        if ( System.currentTimeMillis( ) > liveTimer )
        {

            isAlive = false;
            Remove( );

        }
        else
        {

            int x = this.GetWorldPosition( ).getX( );
            int y = this.GetWorldPosition( ).getY( );

            // animates effect
            this.SetWorldPosition( x, y - 1 );

        }
    }

    public void Remove( )
    {

        // remove this object from handler
        // -> no longer ticks
        Handler.instance.RemoveObject( this );

        // hide
        Hide( );
    }

    public void render( Graphics g ) { if ( isAlive ) Renderer.RenderSprite( sprite, this.GetWorldPosition( ), g ); }

    public Rectangle GetBounds( ) { return null; }
}
