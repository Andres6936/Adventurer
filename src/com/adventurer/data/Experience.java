package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

public class Experience
{

    private int currentExp = 0;
    private int currentLevel = 1;

    // TODO: create static fiels for these.
    private static int baseExp = 100;
    private static double expMultiplier = 1.2;
    private static int maxLevel = 100;
    private Map< Integer, Integer > levelToExp = new LinkedHashMap< Integer, Integer >( );

    public Experience( )
    {
        calculateExpPoints( );
    }

    private void calculateExpPoints( )
    {

        for ( int i = 1; i < maxLevel; i++ )
        {
            Double exp = baseExp * expMultiplier * i;
            int level = i + 1;
            levelToExp.put( level, exp.intValue( ) );
        }

    }

    private void levelUp( )
    {
        currentLevel += 1;
        currentExp = 0;
    }

    public int getNeededExp( int lvl ) { return levelToExp.get( lvl + 1 ); }

    public int getCurrentExp( ) { return currentExp; }

    public void setCurrentExp( int currentExp ) { this.currentExp = currentExp; }

    public void addCurrentExp( int a )
    {
        this.currentExp += a;
        if ( this.currentExp >= levelToExp.get( this.currentLevel + 1 ) )
        {
            levelUp( );
        }
    }

    public int getCurrentLevel( ) { return currentLevel; }

    public void setCurrentLevel( int currentLevel ) { this.currentLevel = currentLevel; }
}
