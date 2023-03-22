package Entity.Mob;

import Entity.Entity;
import Entity.Projectile.Projectile;
import Entity.Projectile.WizardProjectile;
import Graphics.Screen;
import Graphics.Sprite;
import Level.TileCoordinate;


public abstract class Mob extends Entity {
    // Mob means mobile entity
    protected boolean walking = false; //to play moving animations
    // TODO he made a moving boolean in addition to this

    protected int health, mana, experience;

    // Abstract forces subclasses to implement these methods
    public abstract void tick();
    public abstract void render(Screen screen);

    protected enum Direction{
        UP,DOWN,LEFT,RIGHT;
    }

    protected Direction dir;

    protected void move(double xa, double ya){
        if (ya > 0) dir = Direction.UP;
        else if (ya < 0) dir = Direction.DOWN;
        else if (xa > 0) dir = Direction.RIGHT; //swapping 1 and 3 to bottom gives priority to horizontal sprites
        else if (xa < 0) dir = Direction.LEFT;

        while (xa != 0){
            if (Math.abs(xa) >= 1){ // keeps decimals to add up
                if (!collision(abs(xa),ya)){
                    this.x += abs(xa); // allows faster movement with proper collision
                }
                xa -= abs(xa);
            }else{ // Add decimals that will not increment instantly
                if (!collision(abs(xa),ya)){
                    this.x += xa; // allows faster movement with proper collision
                }
                xa = 0;
            }
        }

        while (ya != 0){
            if (Math.abs(ya) >= 1){ // keeps decimals to add up
                if (!collision(xa,abs(ya))){
                    this.y += abs(ya); // allows faster movement with proper collision
                }
                ya -= abs(ya);
            }else{ // Add decimals that will not increment instantly
                if (!collision(xa,abs(ya))){
                    this.y += ya; // allows faster movement with proper collision
                }
                ya = 0;
            }

        }
    }

    protected int abs(double i){
        if (i < 0) return -1;
        return 1;
    }


    protected void shoot(int x, int y, double dir){
        //dir *= 180 /Math.PI; //convert from Rads to Degrees
        Projectile p = new WizardProjectile(x,y,dir);
        level.add(p);
    }

    private boolean collision(double xa, double ya){
        //check each corner of tile
        for (int c = 0; c < 4; c++){ //check current location + movement, divide by 16 to convert from pix to tiles
            double xt = ((x + xa) - c % 2 * 15) / 16;// can't do bitwise operations on doubles ( >> 4)
            double yt = ((y + ya) - c / 2 * 15) / 16; // TODO changed 2 * 16 to 2 * 15 in ep102 for mob not getting stuck in corners
            // Right side corners
            int ix = (int) Math.ceil(xt); // Rounds the number up with Math.ceil
            int iy = (int) Math.ceil(yt);
            if ( c % 2 == 0) ix = (int) Math.floor(xt); // Right side needs diff rounding
            if ( c / 2 == 0) iy = (int) Math.floor(yt); // Same for upside
            if(level.getTile(ix, iy).isSolid()) return true;
        }
        /*
        Changing 2(14) to a larger number increases the width, changing -1(-8) positions the collision box.
        Play with the code, first try it at 0. You'll see the collision zone is just a line.
        Increase it to 10 and it'll be about the size of a tile (depends on your sprites).
        You'll see that the box is the right size, but it's shifted to the left,
        use the second number to shift it right and then tweak the numbers until it's perfect.
        */
        return false;
    }


}
