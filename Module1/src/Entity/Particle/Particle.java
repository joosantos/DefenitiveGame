package Entity.Particle;

import Entity.Entity;
import Graphics.Screen;
import Graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Particle extends Entity {

    private Sprite sprite;

    private int life;

    protected double xx, yy, zz; // zz and za for gravity physics (move down, aka + in Y axis)
    protected double xa, ya, za;// Amount of pixels it moves on each axis

    public Particle (int x, int y, int life){
        this.x = x;
        this.y = y;
        this.xx = x;
        this.yy = y;
        //There are no pointers in java, placing it in array copies it, so set values b4
        this.life = life + (random.nextInt(20) -10); // Give particles varying lifetime
        sprite = Sprite.particle_normal;

        //random between -1 and 1, but closer to 0 most times
        this.xa = random.nextGaussian();
        this.ya = random.nextGaussian();
        this.zz = random.nextFloat() + 2.0; //Makes them move at random speed
    }

    public Particle (int x, int y, int life, Sprite sprite){
        this.x = x;
        this.y = y;
        this.xx = x;
        this.yy = y;
        //There are no pointers in java, placing it in array copies it, so set values b4
        this.life = life + (random.nextInt(20) -10); // Give particles varying lifetime
        this.sprite = sprite;

        //random between -1 and 1, but closer to 0 most times
        this.xa = random.nextGaussian();
        this.ya = random.nextGaussian();
        this.zz = random.nextFloat() + 2.0; //Makes them move at random speed
    }

    //time++;
    //if (time >= Integer.MAX_VALUE-1) time = 0;
    //if (time > life) remove();
    public void tick(){
        life--;
        if (life < 0) remove();
        za -= 0.1;

        if (zz < 0){ // EP 82
            zz = 0;
            za *= -0.55;// make it "bounce" and slow velocity
            // Comment out the xa line for sick effect
            xa *= 0.45;// reduce travel in X and Y axis after bouncing
            ya *= 0.45;// multiplication is better than division
        }

        move(xx + xa, (yy + ya) + (zz + za));
    }

    private void move(double x, double y) {
        if (collision(x, y)){
            this.xa *= 0.5;
            this.ya *= 0.5;
            this.za *= 0.5;
        }
        this.xx += xa;
        this.yy += ya;
        this.zz += za;
    }

    public boolean collision(double x, double y){
        for (int c = 0; c < 4; c++){ // Checks 4 corners
            double xt = (x - c % 2 * 16) / 16;// can't do bitwise operations on doubles ( >> 4)
            double yt = (y - c / 2 * 16) / 16;
            // Right side corners
            int ix = (int) Math.ceil(xt); // Rounds the number up with Math.ceil
            int iy = (int) Math.ceil(yt);
            // Left side corners
            if ( c % 2 == 0) ix = (int) Math.floor(xt); // Right side needs diff rounding
            if ( c / 2 == 0) iy = (int) Math.floor(yt); // Same for upside
            if(level.getTile(ix, iy).isSolid()) return true;
        }
        return false;
    }

    public void render(Screen screen){
        // (xx,yy) holds the location of the projectile + random val
        screen.renderSprite((int) xx - 1, (int) yy - (int) zz - 2, sprite, true); // -1 and -2 are offsets (pixels)
    }

}
