package Level.Tile.Spawn_level;

import Graphics.Screen;
import Graphics.Sprite;
import Level.Tile.Tile;

public class SpawnHedgeTile extends Tile {
    public SpawnHedgeTile(Sprite sprite) {
        super(sprite);
    }

    public void render (int x, int y, Screen screen){
        screen.renderTile(x << 4, y << 4, this); //converting back from tile precision to pixel precision
    }

    public boolean isSolid(){
        return true;
    }

    public boolean breakable() {
        return true;
    }
}
