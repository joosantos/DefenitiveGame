package Entity.Mob;

import Graphics.AnimatedSprite;
import Graphics.Screen;
import Graphics.Sprite;
import Graphics.Spritesheet;
import Level.Node;
import Utils.Vector2i;

import java.util.List;

public class Star extends Enemy{
    private AnimatedSprite up = new AnimatedSprite(Spritesheet.dummy_up, 32, 32, 1);
    private AnimatedSprite right = new AnimatedSprite(Spritesheet.dummy_right, 32, 32, 1);
    private AnimatedSprite down = new AnimatedSprite(Spritesheet.dummy_down, 32, 32, 1);
    private AnimatedSprite left = new AnimatedSprite(Spritesheet.dummy_left, 32, 32, 1);

    private AnimatedSprite animSprite = down;

    private double xa = 0;
    private double ya = 0;
    private double speed = 0.8;

    private List<Node> path = null;
    private int time = 0;

    public Star(int x, int y) {
        this.x = x << 4; // Set to tile precision
        this.y = y << 4;
        sprite = Sprite.mob_forward;
    }

    public void move(){
        xa = 0;// Reset every cycle
        ya = 0;
        //TODO shouldn't need to cast to int, change return type?
        int px = (int)level.getPlayerAt(0).getX(); // get player location, pixel precision, for smooth walking
        int py = (int)level.getPlayerAt(0).getY(); // player position is goal
        Vector2i start = new Vector2i((int)getX() >> 4, (int)getY() >> 4); // convert to tile precision
        Vector2i destination = new Vector2i(px >> 4, py >> 4);
        // Have to keep recalculating the path because players move
        // Don't want to run the A* star algo 60 times per sec, so using local fields (path and time)
        if (time % 6 == 0) path = level.findPath(start, destination); // Update path 10 times per sec
        if (path != null){
            if (path.size() > 0){
                Vector2i vec = path.get(path.size() - 1).tile; // start from last to first
                if (x < vec.getX() << 4) xa++; // multiplying by 16 for pixel precision
                if (x > vec.getX() << 4) xa--;
                if (y < vec.getY() << 4) ya++;
                if (y > vec.getY() << 4) ya--;

            }
        }

        if (xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        }else walking = false;
    }

    public void tick() {
        time ++;
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
