package Entity.Projectile;

import Entity.Entity;
import Graphics.Sprite;

import java.util.Random;

public abstract class Projectile extends Entity {

    protected final double xOrigin, yOrigin;
    protected double angle;
    Sprite sprite;
    protected double x,y; // Need both directional and positional input to be doubles for proper precision when shooting
    protected double newX,newY;
    protected double distance; // Travelled distance from origin
    protected double speed, range, damage; //,rateOfFire

    protected final Random random = new Random(); // For weapons with random range

    public Projectile(double x, double y, double dir){
        xOrigin = x;
        yOrigin = y;
        angle = dir;
        this.x = x;
        this.y = y;
    }

    public Sprite getSprite(){
        return sprite;
    }
    public int getSpriteSize(){
        return sprite.SIZE;
    }
}
