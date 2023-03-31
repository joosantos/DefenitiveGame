package Items;

import Graphics.Sprite;

public class Loot extends Container{

    private Sprite sprite;
    private int x,y;

    public Loot(int size, Sprite sprite, int x, int y) {
        super(size);
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
}
