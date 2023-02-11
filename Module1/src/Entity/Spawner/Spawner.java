package Entity.Spawner;

import Entity.Entity;
import Level.Level;

public class Spawner extends Entity {

    // Not in a separate enum file because will only be used here (for organization purposes)
    protected enum Type { // Doesn't need to be static because compiles it as though it was in its own file
        MOB, PARTICLE
    }

    private Type type;

    public Spawner (int x, int y, Type type, int amount, Level level){
        init(level);
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
