package com.adventurer.data;

import java.awt.Rectangle;

public class Camera
{

    public static Camera instance = null;
    private Rectangle cameraBounds;

    public Camera( )
    {
        if ( instance != null ) return;
        Camera.instance = this;
        this.cameraBounds = new Rectangle( );
    }

    public void Update( Coordinate pos, int width, int height ) { cameraBounds.setBounds( pos.getX( ), pos.getY( ), width, height ); }

    public Rectangle getCameraBounds( ) { return cameraBounds; }
}
