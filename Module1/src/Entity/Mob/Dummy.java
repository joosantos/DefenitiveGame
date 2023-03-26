package Entity.Mob;

import Graphics.AnimatedSprite;
import Graphics.Screen;
import Graphics.Sprite;
import Graphics.Spritesheet;

public class Dummy extends Enemy{

    private AnimatedSprite up = new AnimatedSprite(Spritesheet.dummy_up, 32, 32, 1);
    private AnimatedSprite right = new AnimatedSprite(Spritesheet.dummy_right, 32, 32, 1);
    private AnimatedSprite down = new AnimatedSprite(Spritesheet.dummy_down, 32, 32, 1);
    private AnimatedSprite left = new AnimatedSprite(Spritesheet.dummy_left, 32, 32, 1);

    private AnimatedSprite animSprite = down;

    private int time = 0;
    private int xa = 0;
    private int ya = 0;

    public Dummy(int x, int y){
        this.x = x << 4;
        this.y = y << 4;
        sprite = Sprite.mob_forward;
    }

    public void tick() {
        time ++; // will increment 60 times per second
        if (time % (random.nextInt(50) + 30) == 0){ // at most change direction 2 times per second
            xa = random.nextInt(3) -1; // random.nextInt(3) give 0, 1, or 2
            ya = random.nextInt(3) -1; // -1, 0, or 1

            if (random.nextInt(5) == 0){ // 1 in 5 chance to stop
                xa = 0;
                ya = 0;
            }
        }
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

        if (xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        }else walking = false;

        sprite = animSprite.getSprite();
    }

    public void render(Screen screen) {
        screen.renderMob((int) (x - 16), (int)(y - 16), sprite, 0);
    } // The -16 is same as in Player, fixes collision
}
