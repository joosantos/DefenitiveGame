package Level.Tile;

import Graphics.Screen;
import Graphics.Sprite;

public class RockTile extends Tile {

    public RockTile(Sprite sprite) {
        super(sprite);
    }

    public void render (int x, int y, Screen screen){
        screen.renderTile(x << 4, y << 4, this); //converting back from tile precision to pixel precision
    }


    public boolean isSolid(){ //collision
        return true;
    }
}
