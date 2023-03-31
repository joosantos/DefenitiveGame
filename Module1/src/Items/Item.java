package Items;

import Graphics.Sprite;

public abstract class Item {
    private Sprite sprite;
    public int position;
    private Container container;


    public Item(Sprite sprite, int position, Container container) {
        this.sprite = sprite;
        this.position = position;
        this.container = container;
    }
}
