package Entity;

import Graphics.Screen;
import Graphics.Sprite;
import Level.Level;

import java.security.PublicKey;
import java.util.Random;

public class Entity {

    protected int x, y; // Entities can move
    protected Sprite sprite;
    private boolean removed = false;
    protected Level level;
    protected final Random random = new Random();

    public Entity (){} // So subclasses don't need to use other constructor

    public Entity(int x, int y, Sprite sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public  void tick(){

    }

    public void render(Screen screen){
        if(sprite != null) screen.renderSprite((int) x, (int) y, sprite, true); // Casting it to int auto rounds down
    }

    public void remove(){
        //remove from level
        removed = true;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public boolean isRemoved (){
        return removed;
    }

    public void init(Level level){
        this.level = level;
    }

    public class Spawner {
    }

    public Sprite getSprite(){
        return sprite;
    }
}
