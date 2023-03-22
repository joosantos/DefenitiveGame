package Level.Tile;

import Graphics.Screen;
import Graphics.Sprite;
import Level.Level;

public class PortalTile extends Tile{

    private Level destination;

    public PortalTile(Sprite sprite, Level destination) {
        super(sprite);
        this.destination = destination;
    }

    public void render (int x, int y, Screen screen){
        screen.renderTile(x << 4, y << 4, this); //converting back from tile precision to pixel precision
    }

    public boolean isPortal(){ //collision
        return true;
    }

    public Level enter(){
        // TODO move player to different level
        // TODO remove player from lists
        return this.destination;
    }
}
