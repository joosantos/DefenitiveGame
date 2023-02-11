package Entity.Mob;

import Entity.Entity;
import Graphics.AnimatedSprite;
import Graphics.Screen;
import Graphics.Sprite;
import Graphics.Spritesheet;
import Level.Level;

import java.util.List;

public class Chaser extends Mob{

    private AnimatedSprite up = new AnimatedSprite(Spritesheet.dummy_up, 32, 32, 1);
    private AnimatedSprite right = new AnimatedSprite(Spritesheet.dummy_right, 32, 32, 1);
    private AnimatedSprite down = new AnimatedSprite(Spritesheet.dummy_down, 32, 32, 1);
    private AnimatedSprite left = new AnimatedSprite(Spritesheet.dummy_left, 32, 32, 1);

    private AnimatedSprite animSprite = down;

    private double xa = 0;
    private double ya = 0;
    private double speed = 0.8;

    public Chaser(int x, int y) {
        this.x = x << 4; // Set to tile precision
        this.y = y << 4;
        sprite = Sprite.mob_forward;
    }

    public void move(){
        xa = 0;// Reset every cycle
        ya = 0;
        List<Mob> players = level.getPlayers(this, 50); // TODO was Player, not Mob
        if (players.size() > 0) { // TODO fix stutter stepping
            Mob player = players.get(0); // TODO was Player, not Mob
            if (x < (int)player.getX()) xa+= speed;
            if (x > (int)player.getX()) xa-= speed;
            if (y < (int)player.getY()) ya+= speed;
            if (y > (int)player.getY()) ya-= speed;
        }
        if (xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        }else walking = false;
    }

    public void tick() {
        move();
        if (walking) animSprite.tick();
        else animSprite.setFrame(0); // should be 0, but crashes
        if (xa < 0){
            animSprite = left;
            dir = Direction.LEFT;
        }
        if (xa > 0){
            animSprite = right;
            dir = Direction.RIGHT;
        }
        if (ya < 0) {
            animSprite = up;
            dir = Direction.UP;
        }
        if (ya > 0){
            animSprite = down;
            dir = Direction.DOWN;
        }

        sprite = animSprite.getSprite();
    }

    public void render(Screen screen) { // TODO make class that holds all values like 16, 4, etc
        screen.renderMob((int) (x - 16), (int) (y - 16), this); // The -16 is same as in Player, fixes collision
    }
}
