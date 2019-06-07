package com.adventurer.enumerations;

public enum SpriteType
{

    // actors
    Player,
    GenericEnemy,

    // floor tiles
    Floor01,
    NormalFloor01,
    Grass01,
    Jungle01,
    Water01,
    DeepWater01,
    Sand01,
    TreasuryFloor01,
    MagicFloor01,
    SpawnTile01,

    // traps
    TrapTile01,

    // walls
    Wall01,
    DestructibleWall01,

    // doors
    Door01,
    LockedDoor01,
    LockedDoorDiamond01,

    // portals
    Portal01,
    Portal02,

    // shrines
    HealthShrine_01,
    UsedShrine,

    // chests
    Chest01,
    Chest02,
    LockedChest01,
    LockedChest02,

    // items
    GenericItem,
    Gold01,
    Key,
    DiamondKey,
    HealthPotion,
    ManaPotion,

    // projectiles
    Bomb01,
    Spear01,
    Arrow01,

    // other
    Hit01,
    Heal,
    HealMana,
    Error;
}
