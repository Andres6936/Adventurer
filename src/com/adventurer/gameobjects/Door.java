package com.adventurer.gameobjects;

import com.adventurer.data.Coordinate;
import com.adventurer.data.World;
import com.adventurer.enumerations.DoorType;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.enumerations.TileType;

public class Door extends Tile
{

    protected boolean locked = false;
    protected DoorType doorType;

    public Door( Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, TileType type, boolean locked, DoorType doorType )
    {
        super( worldPos, tilePos, spritetype, type );

        this.doorType = doorType;
        this.locked = locked;
    }

    public void Open( )
    {
        World.instance.ReplaceTile( this, TileType.Floor, SpriteType.Floor01 );
    }

    public void Unlock( )
    {
        this.locked = false;
        World.instance.ChangeTile( this, TileType.Door, SpriteType.Door01 );
    }

    public void Lock( )
    {
        this.locked = true;
        World.instance.ChangeTile( this, TileType.LockedDoor, SpriteType.LockedDoor01 );
    }

    public String toString( )
    {
        String s = "Door";
        if ( this.GetTileType( ) == TileType.LockedDoor ) s = "Locked Door";
        return this.doorType + " " + s;
    }

    public DoorType getDoorType( ) { return this.doorType; }

    public boolean isLocked( ) { return this.locked; }
}
