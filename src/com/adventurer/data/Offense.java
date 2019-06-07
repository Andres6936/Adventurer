package com.adventurer.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.adventurer.enumerations.DamageType;

public class Offense
{

    private Map< DamageType, Integer > meleeWeaponDmg;
    private Map< DamageType, Integer > magicWeaponDmg;
    private Map< DamageType, Integer > rangedWeaponDmg;

    private Map< DamageType, Integer > totalMeleeDmg;

    public Offense( )
    {
        this.meleeWeaponDmg = new LinkedHashMap< DamageType, Integer >( );
        this.magicWeaponDmg = new LinkedHashMap< DamageType, Integer >( );
        this.rangedWeaponDmg = new LinkedHashMap< DamageType, Integer >( );

        this.totalMeleeDmg = new LinkedHashMap< DamageType, Integer >( );
    }

    public Offense( int melee, int magic, int ranged )
    {
        this.meleeWeaponDmg = new LinkedHashMap< DamageType, Integer >( );
        this.magicWeaponDmg = new LinkedHashMap< DamageType, Integer >( );
        this.rangedWeaponDmg = new LinkedHashMap< DamageType, Integer >( );

        this.totalMeleeDmg = new LinkedHashMap< DamageType, Integer >( );

        // populate
        this.totalMeleeDmg.put( DamageType.Physical, melee );
    }

    public int getMagicDmgOfType( DamageType type )
    {
        int dmg = 0;
        if ( this.magicWeaponDmg.containsKey( type ) ) dmg = this.magicWeaponDmg.get( type );
        return dmg;
    }

    public int getRangedDmgOfType( DamageType type )
    {
        int dmg = 0;
        if ( this.rangedWeaponDmg.containsKey( type ) ) dmg = this.rangedWeaponDmg.get( type );
        return dmg;
    }

    public int getMeleeDmgOfType( DamageType type )
    {
        int dmg = 0;
        if ( this.meleeWeaponDmg.containsKey( type ) ) dmg = this.meleeWeaponDmg.get( type );
        return dmg;
    }

    public int getTotalMeleeDmgOfType( DamageType type )
    {
        int dmg = 0;
        if ( this.totalMeleeDmg.containsKey( type ) ) dmg = this.totalMeleeDmg.get( type );
        return dmg;
    }

    public void addMeleeDmgOfType( DamageType type, int dmg )
    {
        int calc = 0;
        if ( this.meleeWeaponDmg.containsKey( type ) ) calc = this.meleeWeaponDmg.get( type );
        this.meleeWeaponDmg.put( type, calc + dmg );
    }

    public void addMagicDmgOfType( DamageType type, int dmg )
    {
        int calc = 0;
        if ( this.magicWeaponDmg.containsKey( type ) ) calc = this.magicWeaponDmg.get( type );
        this.magicWeaponDmg.put( type, calc + dmg );
    }

    public void addRangedDmgOfType( DamageType type, int dmg )
    {
        int calc = 0;
        if ( this.rangedWeaponDmg.containsKey( type ) ) calc = this.rangedWeaponDmg.get( type );
        this.rangedWeaponDmg.put( type, calc + dmg );
    }

    public void setMeleeDmgOfType( DamageType type, int dmg ) { this.meleeWeaponDmg.put( type, dmg ); }

    public void setMagicDmgOfType( DamageType type, int dmg ) { this.magicWeaponDmg.put( type, dmg ); }

    public void setRangedDmgOfType( DamageType type, int dmg ) { this.rangedWeaponDmg.put( type, dmg ); }

    public void setTotalMeleeDmgOfType( DamageType type, int dmg ) { this.totalMeleeDmg.put( type, dmg ); }

    public Map< DamageType, Integer > getAllMeleeDamageTypes( ) { return this.meleeWeaponDmg; }

    public Map< DamageType, Integer > getAllMagicDamageTypes( ) { return this.magicWeaponDmg; }

    public Map< DamageType, Integer > getAllRangedDamageTypes( ) { return this.rangedWeaponDmg; }

    public Map< DamageType, Integer > getTotalMeleeDmg( ) { return this.totalMeleeDmg; }
}
