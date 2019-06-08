package com.adventurer.utilities;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.adventurer.main.Game;

public class Window extends Canvas
{

    private static final long serialVersionUID = 351245801233048538L;

    private String title;
    private JFrame frame;

    public Window( int width, int height, String title, Game game )
    {

        this.title = title;

        // create a new frame
        frame = new JFrame( title );

        // set the dimensions of the frame
        frame.setPreferredSize( new Dimension( width, height ) );
        frame.setMinimumSize( new Dimension( width, height ) );
        frame.setMaximumSize( new Dimension( width, height ) );

        // set closing operation
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // set if the frame is resizeable
        frame.setResizable( false );

        // set the location
        frame.setLocationRelativeTo( null );

        // set frame icon
        ImageIcon img = new ImageIcon( Game.IMAGES_ICON_PNG );
        frame.setIconImage( img.getImage( ) );

        // add our game to the frame
        frame.add( game );

        // pack the frame
        frame.pack( );

        // set the frame to be visible.
        frame.setVisible( true );

        // start the game
        game.Start( );
    }

    public JFrame getFrame( ) { return this.frame; }

    public void SetCustomTitle( String text ) { frame.setTitle( title + ", " + text ); }
}
