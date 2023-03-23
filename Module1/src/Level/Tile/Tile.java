package Level.Tile;

import Graphics.Screen;
import Graphics.Sprite;
import Level.RandomLevel;
import Level.Tile.Spawn_level.*;

public class Tile {
    public Sprite sprite;

    public static Tile grass = new GrassTile(Sprite.grass);
    public static Tile flower = new FlowerTile(Sprite.flower);
    public static Tile rock = new RockTile(Sprite.rock);
    public static Tile voidTile = new VoidTile(Sprite.voidSprite);

    public static Tile spawn_grass = new SpawnGrassTile(Sprite.spawn_grass);
    public static Tile spawn_hedge = new SpawnHedgeTile(Sprite.spawn_hedge);
    public static Tile spawn_water = new SpawnWaterTile(Sprite.spawn_water);
    public static Tile spawn_wall1 = new SpawnWallTile(Sprite.spawn_wall1);
    public static Tile spawn_wall2 = new SpawnWallTile(Sprite.spawn_wall2);
    public static Tile spawn_floor_boards = new SpawnFloorTile(Sprite.spawn_floor_boards);
    public static Tile spawn_floor = new SpawnFloorTile(Sprite.spawn_floor);

    public static Tile spawn_portal = new PortalTile(Sprite.spawn_portal, (short) 12345);


    public static final int col_spawn_grass = 0xFF23FF44;
    public static final int col_spawn_wall1 = 0xFF4A5D67;
    public static final int col_spawn_hedge = 0xFF117A20;
    public static final int col_spawn_floor = 0xFF5F2D01;
    public static final int col_spawn_wall2 = 0; //unused
    public static final int col_spawn_water = 0; //unused
    public static final int col_spawn_floor_boards = 0xFF864815;
    public static final int col_spawn_portal = 0xFFFF2323;


    public Tile (Sprite sprite){ //forces all Tiles to have a sprite
        this.sprite = sprite;
    }

    public void render (int x, int y, Screen screen){
    }

    public boolean isSolid(){ //collision
        return false;
    }

    public boolean isPortal(){ //collision
        return false;
    }

}
