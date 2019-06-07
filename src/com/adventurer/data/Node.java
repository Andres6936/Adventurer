package com.adventurer.data;

public class Node
{

    private double fScore = Double.POSITIVE_INFINITY, gScore = Double.POSITIVE_INFINITY;

    public void reset( )
    {
        this.setfScore( Double.POSITIVE_INFINITY );
        this.setgScore( Double.POSITIVE_INFINITY );
    }

    public double getfScore( ) { return fScore; }

    public double getgScore( ) { return gScore; }

    public void setfScore( double fScore ) { this.fScore = fScore; }

    public void setgScore( double gScore ) { this.gScore = gScore; }
}
