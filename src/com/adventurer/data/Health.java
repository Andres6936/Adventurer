package com.adventurer.data;

public class Health
{

    private int currentHP = 0;
    private int maxHP = 100;

    private boolean dead = false;

    public Health( int maxHP )
    {
        this.maxHP = maxHP;
        this.currentHP = this.maxHP;
    }

    public boolean TakeDamage( int damage )
    {
        this.currentHP -= damage;
        if ( this.currentHP <= 0 )
        {
            this.currentHP = 0;
            dead = true;
        }
        return this.dead;
    }

    public void healDamage( int a )
    {
        this.currentHP += a;
        if ( this.currentHP > this.maxHP ) this.currentHP = this.maxHP;
    }

    public boolean isDead( ) { return this.dead; }

    public int GetCurrentHealth( ) { return this.currentHP; }

    public int GetMaxHP( ) { return this.maxHP; }

    public void setCurrentHP( int a ) { this.currentHP = a; }

    public void setMaxHP( int a ) { this.maxHP = a; }
}
