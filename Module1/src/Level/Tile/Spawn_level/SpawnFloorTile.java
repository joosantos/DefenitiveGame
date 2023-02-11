package Level.Tile.Spawn_level;

import Graphics.Screen;
import Graphics.Sprite;
import Level.Tile.Tile;

public class SpawnFloorTile extends Tile {
    public SpawnFloorTile(Sprite sprite) {
        super(sprite);
    }

    public void render (int x, int y, Screen screen){
        screen.renderTile(x << 4, y << 4, this); //converting back from tile precision to pixel precision
    }
}
