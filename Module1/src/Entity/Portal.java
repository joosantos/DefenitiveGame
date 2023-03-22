package Entity;

import Graphics.Sprite;
import Level.Level;

public class Portal extends Entity{

    private Level destination;
    private int sizeX,sizeY;

    public Portal(int x, int y, Sprite sprite, Level destination) {
        super(x, y, sprite);
        this.destination = destination;
        sizeX = sizeY = 10;
    }

    public Level enter(){
        // TODO move player to different level
        return this.destination;
    }

    // TODO add listener for player collision, to show enter button
}
