package com.adventurer.utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.data.ParseData;

public class RichTextParser
{

    // Parser supports command syntax:
    // <color="200,200,200"> ... </color>

    public static ParseData parseStringColor( String inputString )
    {

        ParseData data = new ParseData( );

        // Parser supports command syntax:
        // 1. <color="200,200,200"> ... </color>

        boolean startCmd = false, hasStartCmdEnded = false, hasStartCmdTerminated = false,
                endCmd = false, hasEndCmdEnded = false,
                paramStarted = false, paramEnded = false;

        int count = - 1, startCommandPos = 0, endCommandPos = 0;

        List< Character > cmd = new ArrayList< Character >( );
        List< Character > endcmd = new ArrayList< Character >( );
        List< Character > parameter = new ArrayList< Character >( );
        List< Character > content = new ArrayList< Character >( );

        // parsing happens here
        for ( char c : inputString.toCharArray( ) )
        {

            count++;

            // TODO: this means we can only parse one color command per string.
            if ( hasEndCmdEnded ) break;

            // parse end command
            if ( endCmd && hasEndCmdEnded == false )
            {

                if ( c == '>' )
                {
                    hasEndCmdEnded = true;
                    endCommandPos = count + 1;
                }
                else { endcmd.add( c ); }

                continue;

            }

            // content
            if ( hasStartCmdTerminated && endCmd == false )
            {

                if ( c == '<' ) { endCmd = true; }
                else { content.add( c ); }

                continue;
            }

            // end start command
            if ( c == '>' && paramEnded )
            {
                hasStartCmdTerminated = true;

                continue;
            }

            // parse start command
            if ( startCmd && hasStartCmdEnded == false )
            {

                if ( c == '=' ) { hasStartCmdEnded = true; }
                else { cmd.add( c ); }

                continue;

            }
            else if ( startCmd && hasStartCmdEnded )
            {

                // parse command parameter
                if ( c == '"' && paramStarted == false )
                {
                    paramStarted = true; // first '"'
                }
                else if ( c == '"' && paramStarted )
                {
                    paramEnded = true;       // closing '"'
                }
                else if ( paramEnded == false ) parameter.add( c );           // cache parameter

                continue;
            }


            // check whether we should start parsing a command
            if ( c == '<' && startCmd == false )
            {
                startCmd = true;
                startCommandPos = count;
            }
        }

        if ( startCmd && hasStartCmdEnded && endCmd && hasEndCmdEnded )
        {

            // after parsing the string, we have to test if the cmd is the same as endcmd.
            // if the commands are same, it's a complete and valid command.

            // remove the ending '/'
            endcmd.remove( 0 );

            if ( cmd.equals( endcmd ) )
            {

                // create parameter string
                String param = "";
                for ( char c : parameter ) { param += c; }

                //https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
                param = param.replaceAll( "\\s+", "" );

                // get color channels
                String[] params = param.split( "," );
                int r = Integer.parseInt( params[ 0 ] );
                int g = Integer.parseInt( params[ 1 ] );
                int b = Integer.parseInt( params[ 2 ] );

                // modify color with parsed data
                Color color = new Color( r, g, b, 255 );

                // create a string that doesn't have the rich-text commands.
                StringBuilder builder = new StringBuilder( inputString );            // create a stringbuilder
                builder = builder.delete( startCommandPos, endCommandPos );           // delete the whole command, other parts of the string are left untouched.
                String content_ = "";                                               // the content will be cached here
                for ( char c : content ) content_ += c;                               // create content string
                String ss = builder.insert( startCommandPos, content_ ).toString( );  // insert the content in right position

                // get the start and end positions of the string we want to color differently.
                int[] pos = new int[]{ startCommandPos - 1, startCommandPos + content_.length( ) };

                // add color, string and positions to data class
                data.setColor( color );
                data.setString( ss );
                data.setPositions( pos );

            }
            else { data = null; }

        }

        // return parsed color and content
        return data;
    }

}
