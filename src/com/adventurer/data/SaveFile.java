package com.adventurer.data;

import com.adventurer.utilities.FileReader;
import com.adventurer.utilities.FileWriter;

/*
 * This class gets the information about the
 * permanent state of the game.
 *
 */

public class SaveFile
{

    public static final String SAVEFILENAME = "savefile";
    private int diamondKeyCount;

    public SaveFile( )
    {
        String data = FileReader.readSaveFile( );
        int dcount = Integer.parseInt( data.substring( 7, 8 ) );
        this.setDiamondKeyCount( dcount );
    }

    public void addDiamondKeyCount( int a )
    {
        this.diamondKeyCount += a;
        FileWriter.writeSaveFileData( this );
    }

    public int getDiamondKeyCount( ) { return diamondKeyCount; }

    public void setDiamondKeyCount( int diamondKeyCount ) { this.diamondKeyCount = diamondKeyCount; }
}
