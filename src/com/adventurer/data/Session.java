package com.adventurer.data;

import com.adventurer.main.Game;
import com.adventurer.utilities.FileWriter;

public class Session
{

    private int score;
    private String sessionName;
    private int dungeonLevel = 1;

    public Session( String sessionName )
    {
        this.setSessionName( sessionName );
        this.setScore( 0 );
    }

    // this is called when the "session/run" is over.
    public void saveSessionData( )
    {
        FileWriter.writeSessionData( this );
        FileWriter.writeSaveFileData( Game.instance.getCurrentSaveFile( ) );
    }

    public void addScore( int a ) { this.setScore( this.getScore( ) + a ); }

    public void setSessionName( String sessionName ) { this.sessionName = sessionName; }

    public void setScore( int score ) { this.score = score; }

    public void addDungeonLevel( int a ) { this.dungeonLevel += a;}

    public void setDungeonLevel( int dungeonLevel ) { this.dungeonLevel = dungeonLevel;}

    public String getSessionName( ) { return sessionName; }

    public int getScore( ) { return score; }

    public int getDungeonLevel( ) { return dungeonLevel; }
}
