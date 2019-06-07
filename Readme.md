# Adventurer - Roguelite Game
Adventurer is a roguelite game, that uses realtime tile-based movement system. It is not turn-based and therefore it is not a rogueLIKE game. it's created 100% with pure Java and therefore it doesn't use any game libraries. Sprites are created with pyxelEdit-software. 

## Screenshots (21.7.2017)
![screeenshot-1](https://user-images.githubusercontent.com/7894317/28478002-32446676-6e5f-11e7-90cc-c32180b693ff.PNG)
![screeenshot-2](https://user-images.githubusercontent.com/7894317/28478016-446829b4-6e5f-11e7-8a87-92eef5c728dd.PNG)
![screeenshot-3](https://user-images.githubusercontent.com/7894317/28478020-47a48442-6e5f-11e7-8297-6fe162b0ed80.PNG)

## Tech
The game uses simple 2D game engine written in Java, that is based on Notch's work, but also has my own code and some snippets from other sites. Every snippet, that is not mine, has a link to the original owner.

### Features
Engine supports these features:
* Spritesheet sprite drawing (16x16)
* Rendering queue (z-levels)
* Camera: location and zoom-level.
* Viewport moves with camera
* Gameobjects that are inside camera's view render, others don't. 
* ...

At this moment the game supports these features:
* Dungeon generation from predefined 2d char arrays.
* Random dungeon generation (Boring, needs more work).
* Projectiles
* Turrets that shoot projectiles
* Line of sight calculations: usees Bresenham's line algorithm.
* Enemies use A*-pathing algorithm.
* Enemy types: some enemies can shoot projectiles, some are only melee and some create bombs on death.
* Tile based system: tile can have actor, item and multiple vanity items on it.
* Tile types: Traps (gas & projectile), doors (unlocked & locked), portals, walls (undestructible & destructible), floor.
* Inventory-system: keys (normal & diamond), bombs, projectiles
* Session files & permanent save files
* Persistency: what ever the player gained from a single session can be saved into a persistent save file (such as diamond keys).
* ... 

## TODO
There is a lot of work to do here. At the moment multiple bugs and crashes are the number one on my TODO-list.

Features that are necessary in the future:
* Some sort of random dungeon generation
* Better GUI
* Main menu
* Bosses
* ...

## How to Contribute
See contributing.md for more info, but in short you can submit new ideas and features to me via twitter (@Baserfaz). I'm more than glad to read your ideas.

## Contributors
* Heikki Heiskanen (Baserfaz) - Initial work

## Info and Contact
* [Heikki Heiskanen](http://www.heiskanenheikki.fi)
* [Baserfaz projects](http://baserfaz.github.io/Projects/)
* [Twitter](https://twitter.com/Baserfaz)
* [LinkedIn](https://www.linkedin.com/in/heikki-heiskanen-140675a1/)

### TODO

ADVENTURER - ROGUELIKE
TODO LIST
1.  forbid enemy spawning on the room where player spawned.
2.  killing enemies, opening chests gives score.
3.  death screen GUI.
4.  main menu GUI.
5.  chest spawning in rooms + locked chests in treasure rooms.
6.  enemies drop items
7.  stats-system (str, int, dex, HP) 
-> str: melee damage
-> int: magic damage
-> dex: ranged damage
-> HP: more health obviously.
8.  simple "inventory/equipment"-system, where player can only wield one weapon and few pieces of equipment.
9.  picking up items from the floor.
10. golden key spawning/dropping randomly
11. golden keys can open golden chests -> better loot/more points?
12. create xml-parser for items
13. create xml-file for items.
14. health shrines -> 10% and 100% ones. Full health shrine located at lobby.
15. simple storage system for items in lobby.
16. diamond keys drop from bosses?? (diamond keys open up new dungeons or can be exchanged for goods in lobby)
17. ---> bosses?
18. session points do what exactly??? Think something cool.
KNOWN BUGS:
1. all tiles flash visible sometimes when attacking enemy
2. problem with portal and enemy los. (skeleton throws spear in wrong dir and still hits player.)
