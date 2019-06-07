package com.adventurer.data;

import java.awt.Color;

public class ParseData
{

    private String string;
    private Color color;
    private int[] positions;

    public ParseData( )
    {
        this.string = null;
        this.color = null;
        this.positions = new int[ 2 ];
    }

    public ParseData( String string, Color color, int[] positions )
    {
        this.string = string;
        this.color = color;
        this.positions = positions;
    }

    public String getString( ) { return string; }

    public Color getColor( ) { return color; }

    public int[] getPositions( ) { return positions; }

    public void setString( String string ) { this.string = string; }

    public void setColor( Color color ) { this.color = color; }

    public void setPositions( int[] positions ) { this.positions = positions; }

}
