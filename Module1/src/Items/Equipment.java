package Items;

import Graphics.Sprite;

public abstract class Equipment extends Item{
    protected enum Type{
        WEAPON, ARMOUR, ABILITY, RING;
    }

    protected Type type;

    public Equipment(Sprite sprite, int position, Container container, Type type) {
        super(sprite, position, container);
        this.type = type;
    }
}
