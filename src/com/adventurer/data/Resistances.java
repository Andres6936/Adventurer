package com.adventurer.data;

import java.util.Map;
import java.util.Map.Entry;

import com.adventurer.enumerations.DamageType;

public class Resistances
{

    private int physicalResistance;
    private int fireResistance;
    private int frostResistance;
    private int shockResistance;
    private int holyResistance;

    public Resistances( )
    {
        this.physicalResistance = 0;
        this.fireResistance = 0;
        this.frostResistance = 0;
        this.shockResistance = 0;
        this.holyResistance = 0;
    }

    public Resistances( int phys, int fire, int frost, int shock, int holy )
    {
        this.physicalResistance = phys;
        this.fireResistance = fire;
        this.frostResistance = frost;
        this.shockResistance = shock;
        this.holyResistance = holy;
    }

    public Resistances( Map< DamageType, Integer > resistances )
    {
        for ( Entry< DamageType, Integer > e : resistances.entrySet( ) )
        {
            DamageType key = e.getKey( );
            int value = e.getValue( );

            switch ( key )
            {
                case Fire:
                    this.fireResistance = value; break;
                case Frost:
                    this.frostResistance = value; break;
                case Holy:
                    this.holyResistance = value; break;
                case Shock:
                    this.shockResistance = value; break;
                case Physical:
                    this.physicalResistance = value; break;
                default:
                    System.out.println( "DAMAGETYPE OF " + key.toString( ) + " NO YET IMPLEMENTED!" ); break;
            }
        }
    }

    public int getFireResistance( ) { return fireResistance; }

    public void setFireResistance( int fireResistance ) { this.fireResistance = fireResistance; }

    public void addFireResistance( int a ) { this.fireResistance += a; }

    public int getFrostResistance( ) { return frostResistance; }

    public void setFrostResistance( int frostResistance ) { this.frostResistance = frostResistance; }

    public void addFrostResistances( int a ) { this.frostResistance += a; }

    public int getShockResistance( ) { return shockResistance; }

    public void setShockResistance( int shockResistance ) { this.shockResistance = shockResistance; }

    public void addShockResistance( int a ) { this.shockResistance += a; }

    public int getHolyResistance( ) { return holyResistance; }

    public void setHolyResistance( int holyResistance ) { this.holyResistance = holyResistance; }

    public void addHolyResistance( int a ) { this.holyResistance += a; }

    public int getPhysicalResistance( ) { return physicalResistance; }

    public void setPhysicalResistance( int physicalResistance ) { this.physicalResistance = physicalResistance; }

    public void addPhysicalResistance( int a ) { this.physicalResistance += a; }

}
