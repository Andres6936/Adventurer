package com.adventurer.utilities;

// IO

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

// XML
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

import com.adventurer.data.SaveFile;
import com.adventurer.enumerations.RootElement;

public class FileReader
{

    public static Map< String, String > readXMLGameData( String key, RootElement rootElement )
    {
        return readXML( "resources/data/gamedata.xml", key, rootElement );
    }

    // https://stackoverflow.com/questions/428073/what-is-the-best-simplest-way-to-read-in-an-xml-file-in-java-application
    public static Map< String, String > readXML( String filename, String key, RootElement rootElement )
    {

        Map< String, String > myMap = new LinkedHashMap< String, String >( );

        try
        {

            // ---------
            File xmlfile = new File( filename );
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance( );
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder( );
            Document doc = dBuilder.parse( xmlfile );
            doc.getDocumentElement( ).normalize( );
            // ---------

            // TODO: accurate enemy search i.e. by name
            // 1. enemies are searched by EnemyType (used to get a random enemy of type)
            // 2. items are searched by item's name.

            NodeList root = doc.getElementsByTagName( rootElement.toString( ) );

            for ( int i = 0; i < root.getLength( ); i++ )
            {

                Node n = root.item( i );

                if ( rootElement == RootElement.enemy )
                {

                    if ( n.getNodeType( ) == Node.ELEMENT_NODE )
                    {

                        Element e = ( Element ) n;

                        if ( e.getElementsByTagName( "enemyType" ).item( 0 ).getTextContent( ).equals( key ) )
                        {

                            String name = e.getElementsByTagName( "name" ).item( 0 ).getTextContent( );
                            String enemyType = e.getElementsByTagName( "enemyType" ).item( 0 ).getTextContent( );
                            String health = e.getElementsByTagName( "health" ).item( 0 ).getTextContent( );
                            String damage = e.getElementsByTagName( "damage" ).item( 0 ).getTextContent( );
                            String isRanged = e.getElementsByTagName( "isRanged" ).item( 0 ).getTextContent( );
                            String movementSpeed = e.getElementsByTagName( "movementSpeed" ).item( 0 ).getTextContent( );
                            String movementCooldownBase = e.getElementsByTagName( "movementCooldownBase" ).item( 0 ).getTextContent( );
                            String exp = e.getElementsByTagName( "exp" ).item( 0 ).getTextContent( );

                            myMap.put( "name", name );
                            myMap.put( "enemyType", enemyType );
                            myMap.put( "health", health );
                            myMap.put( "damage", damage );
                            myMap.put( "isRanged", isRanged );
                            myMap.put( "movementSpeed", movementSpeed );
                            myMap.put( "movementCooldownBase", movementCooldownBase );
                            myMap.put( "exp", exp );

                            // resistances
                            NodeList list = e.getElementsByTagName( "resistances" ).item( 0 ).getChildNodes( );

                            // get defense values
                            for ( int j = 0; j < list.getLength( ); j++ )
                            {

                                // get node
                                Node currentNode = list.item( j );
                                if ( currentNode.getNodeType( ) != Node.ELEMENT_NODE ) continue;

                                // cast node to element.
                                Element currentElement = ( Element ) currentNode;

                                String nodeName = currentElement.getNodeName( );
                                String content = currentElement.getTextContent( );

                                myMap.put( nodeName, content );
                            }

                            break;
                        }
                    }

                }
                else if ( rootElement == RootElement.armor )
                {

                    if ( n.getNodeType( ) == Node.ELEMENT_NODE )
                    {

                        Element e = ( Element ) n;

                        if ( e.getElementsByTagName( "name" ).item( 0 ).getTextContent( ).equals( key ) )
                        {

                            String name = e.getElementsByTagName( "name" ).item( 0 ).getTextContent( );
                            String description = e.getElementsByTagName( "description" ).item( 0 ).getTextContent( );
                            String value = e.getElementsByTagName( "value" ).item( 0 ).getTextContent( );

                            myMap.put( "name", name );
                            myMap.put( "description", description );
                            myMap.put( "value", value );

                            NodeList list = e.getElementsByTagName( "defenseValues" ).item( 0 ).getChildNodes( );

                            // get defense values
                            for ( int j = 0; j < list.getLength( ); j++ )
                            {

                                // get node
                                Node currentNode = list.item( j );
                                if ( currentNode.getNodeType( ) != Node.ELEMENT_NODE ) continue;

                                // cast node to element.
                                Element currentElement = ( Element ) currentNode;

                                String nodeName = currentElement.getNodeName( );
                                String content = currentElement.getTextContent( );

                                myMap.put( nodeName, content );
                            }

                            break;
                        }
                    }

                }
                else if ( rootElement == RootElement.weapon )
                {

                    if ( n.getNodeType( ) == Node.ELEMENT_NODE )
                    {

                        Element e = ( Element ) n;

                        if ( e.getElementsByTagName( "name" ).item( 0 ).getTextContent( ).equals( key ) )
                        {

                            String name = e.getElementsByTagName( "name" ).item( 0 ).getTextContent( );
                            String description = e.getElementsByTagName( "description" ).item( 0 ).getTextContent( );
                            String value = e.getElementsByTagName( "value" ).item( 0 ).getTextContent( );
                            String weaponSlot = e.getElementsByTagName( "weaponSlot" ).item( 0 ).getTextContent( );
                            String weaponType = e.getElementsByTagName( "weaponType" ).item( 0 ).getTextContent( );

                            myMap.put( "name", name );
                            myMap.put( "description", description );
                            myMap.put( "value", value );
                            myMap.put( "weaponSlot", weaponSlot );
                            myMap.put( "weaponType", weaponType );

                            NodeList list = e.getElementsByTagName( "damageValues" ).item( 0 ).getChildNodes( );

                            // get damage values
                            for ( int j = 0; j < list.getLength( ); j++ )
                            {

                                // get node
                                Node currentNode = list.item( j );
                                if ( currentNode.getNodeType( ) != Node.ELEMENT_NODE ) continue;

                                // cast node to element.
                                Element currentElement = ( Element ) currentNode;

                                String nodeName = currentElement.getNodeName( );
                                String content = currentElement.getTextContent( );

                                myMap.put( nodeName, content );
                            }

                            break;
                        }
                    }
                }
            }

        }
        catch ( ParserConfigurationException | SAXException | IOException e ) { e.printStackTrace( ); }
        return myMap;
    }

    public static String readSaveFile( ) { return readFile( SaveFile.SAVEFILENAME + ".txt" ); }

    public static String readFile( String filename )
    {
        String content = "";
        try { content = new String( Files.readAllBytes( Paths.get( "data/" + filename ) ) ); }
        catch ( NoSuchFileException e )
        {
            if ( filename == SaveFile.SAVEFILENAME + ".txt" )
            {

                // there is no savefile created yet!
                // --> create empty file
                FileWriter.createSaveFile( );

                // read the default file.
                content = readSaveFile( );
            }
        }
        catch ( IOException e ) { e.printStackTrace( ); }
        return content;
    }
}
