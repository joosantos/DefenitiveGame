package Level.Tile;

import Graphics.Screen;
import Graphics.Sprite;

public class FlowerTile extends Tile {
    public FlowerTile(Sprite sprite) {
        super(sprite);
    }

    public void render (int x, int y, Screen screen){
        screen.renderTile(x << 4, y << 4, this); //converting back from tile precision to pixel precision
    }
}
