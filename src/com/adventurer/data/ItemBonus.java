package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.enumerations.DamageType;

public class ItemBonus
{

    private Map< DamageType, Integer > damageValues = null;
    private Map< DamageType, Integer > resistanceValues = null;

    private int strBonus, dexBonus, intBonus, vitBonus;

    public ItemBonus( )
    {
        this.damageValues = new LinkedHashMap< DamageType, Integer >( );
        this.resistanceValues = new LinkedHashMap< DamageType, Integer >( );

        this.strBonus = 0;
        this.dexBonus = 0;
        this.intBonus = 0;
        this.vitBonus = 0;
    }

    public ItemBonus( Map< DamageType, Integer > myMap, boolean isResistance )
    {

        this.resistanceValues = new LinkedHashMap< DamageType, Integer >( );
        this.damageValues = new LinkedHashMap< DamageType, Integer >( );

        if ( isResistance ) { this.resistanceValues = new LinkedHashMap< DamageType, Integer >( myMap ); }
        else { this.damageValues = new LinkedHashMap< DamageType, Integer >( myMap ); }

        this.strBonus = 0;
        this.dexBonus = 0;
        this.intBonus = 0;
        this.vitBonus = 0;

    }

    public ItemBonus( Map< DamageType, Integer > resistances, Map< DamageType, Integer > dmg )
    {

        if ( dmg != null ) { this.damageValues = new LinkedHashMap< DamageType, Integer >( dmg ); }
        else { this.damageValues = new LinkedHashMap< DamageType, Integer >( ); }

        if ( resistances != null ) { this.resistanceValues = new LinkedHashMap< DamageType, Integer >( resistances ); }
        else { this.resistanceValues = new LinkedHashMap< DamageType, Integer >( ); }

        this.strBonus = 0;
        this.dexBonus = 0;
        this.intBonus = 0;
        this.vitBonus = 0;

    }

    public void swapDmgAndRes( boolean dmgToRes )
    {

        if ( dmgToRes )
        {

            this.resistanceValues = new LinkedHashMap< DamageType, Integer >( this.damageValues );
            this.damageValues.clear( );

        }
        else
        {

            this.damageValues = new LinkedHashMap< DamageType, Integer >( this.resistanceValues );
            this.resistanceValues.clear( );

        }

    }

    public Map< DamageType, Integer > getDamage( ) { return damageValues; }

    public void setDamage( Map< DamageType, Integer > bonusDamage ) { this.damageValues = bonusDamage; }

    public Map< DamageType, Integer > getResistances( ) { return resistanceValues; }

    public void setResistances( Map< DamageType, Integer > bonusResistance ) { this.resistanceValues = bonusResistance; }

    public void setDamageOfType( DamageType type, int dmg ) { this.damageValues.put( type, dmg ); }

    public void setResistanceOfType( DamageType type, int res ) { this.resistanceValues.put( type, res ); }

    public int getStrBonus( ) { return strBonus; }

    public void setStrBonus( int strBonus ) { this.strBonus = strBonus; }

    public int getDexBonus( ) { return dexBonus; }

    public void setDexBonus( int dexBonus ) { this.dexBonus = dexBonus; }

    public int getIntBonus( ) { return intBonus; }

    public void setIntBonus( int intBonus ) { this.intBonus = intBonus;}

    public int getVitBonus( ) { return vitBonus; }

    public void setVitBonus( int vitBonus ) { this.vitBonus = vitBonus; }

}
