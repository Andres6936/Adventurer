package com.adventurer.utilities;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.adventurer.data.SaveFile;
import com.adventurer.data.Session;
import com.adventurer.main.Game;

public class FileWriter
{

    // create a default file
    // DONT USE THIS.
    public static void createSaveFile( )
    {
        try
        {
            new File( "data" ).mkdirs( );
            PrintWriter writer = new PrintWriter( "data/" + Game.instance.getCurrentSaveFile( ).SAVEFILENAME + ".txt", "UTF-8" );
            writer.println( "dkeys: " + Game.START_DIAMOND_KEY_COUNT );
            writer.close( );
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
            System.exit( 1 );
        }
    }

    public static void writeSaveFileData( SaveFile saveFile )
    {
        try
        {
            new File( "data" ).mkdirs( );
            PrintWriter writer = new PrintWriter( "data/" + saveFile.SAVEFILENAME + ".txt", "UTF-8" );
            writer.println( "dkeys: " + saveFile.getDiamondKeyCount( ) );
            writer.close( );
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
            System.exit( 1 );
        }
    }

    public static void writeSessionData( Session session )
    {
        try
        {
            new File( "data/sessions" ).mkdirs( );
            PrintWriter writer = new PrintWriter( "data/sessions/" + session.getSessionName( ) + ".txt", "UTF-8" );
            writer.println( "Session name: " + session.getSessionName( ) );
            writer.println( "Score: " + session.getScore( ) );
            writer.close( );
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
            System.exit( 1 );
        }
    }
}
