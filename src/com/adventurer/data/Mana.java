package com.adventurer.data;

public class Mana
{

    private int currentMP = 0;
    private int maxMP = 10;

    public Mana( int maxMP )
    {
        this.maxMP = maxMP;
        this.currentMP = this.maxMP;
    }

    public void useMP( int amount )
    {
        this.currentMP -= amount;
        if ( this.currentMP <= 0 )
        {
            this.currentMP = 0;
        }
    }

    public void addMP( int a )
    {
        this.currentMP += a;
        if ( this.currentMP > this.maxMP ) this.currentMP = this.maxMP;
    }

    public int GetCurrentMana( ) { return this.currentMP; }

    public int GetMaxMP( ) { return this.maxMP; }

    public void setCurrentMP( int a ) { this.currentMP = a; }

    public void setMaxMP( int a ) { this.maxMP = a; }
}
