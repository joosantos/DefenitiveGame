package Items;

import Graphics.Sprite;

public abstract class Consumable extends Item{
    private int stackSize;

    public Consumable(Sprite sprite, int position, Container container, int size) {
        super(sprite, position, container);
        stackSize = size;
    }

    public abstract void consume();
}
