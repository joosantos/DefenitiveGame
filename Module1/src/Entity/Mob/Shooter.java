package Entity.Mob;

import Entity.Entity;
import Graphics.AnimatedSprite;
import Graphics.Screen;
import Graphics.Sprite;
import Graphics.Spritesheet;
import Utils.Debug;
import Utils.Vector2i;

import java.util.List;

public class Shooter extends Mob{

    private AnimatedSprite up = new AnimatedSprite(Spritesheet.dummy_up, 32, 32, 1);
    private AnimatedSprite right = new AnimatedSprite(Spritesheet.dummy_right, 32, 32, 1);
    private AnimatedSprite down = new AnimatedSprite(Spritesheet.dummy_down, 32, 32, 1);
    private AnimatedSprite left = new AnimatedSprite(Spritesheet.dummy_left, 32, 32, 1);

    private AnimatedSprite animSprite = down;

    private int time = 0;
    private int xa = 0;
    private int ya = 0;

    private Entity rand = null;

    public Shooter (int x, int y){
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
            //move(xa, ya);
            walking = true;
        }else walking = false;

        sprite = animSprite.getSprite();

        //Shoot
        shootRandom();
    }

    private void shootRandom(){
        List<Entity> entities = level.getEntities(this, 500);
        entities.add(level.getClientPlayer()); // add the client player, TODO why is it needed, should already be in entities no?
        // TODO in future remove the client player addition and make a check to see if entities.size > 0, else dont run rest of code
        if(time % (30 + random.nextInt(91)) == 0){ // up to 2 secs
            // Decide what to shoot at
            int index = random.nextInt(entities.size());
            rand = entities.get(index);
        }
        if (rand != null){
            double dx = rand.getX() - x; // Clac distances in a triangle
            double dy = rand.getY() - y;
            //TODO make sprite change based on shooting direction over walking
            double dirShoot = Math.atan2(dy, dx); // Get angle
            shoot(x, y, dirShoot);
        }

        /* Alternative
        *         if(time % (30 + random.nextInt(91)) == 0){ // up to 2 secs
            List<Entity> entities = level.getEntities(this, 500);
            entities.add(level.getClientPlayer()); // add the client player, TODO why is it needed, should already be in entities no?
            // Decide what to shoot at
            Collections.shuffle(entities); // Randomize the order


            if (entities.size() > 0){
                rand = entities.get(0);
                double dx = rand.getX() - x; // Clac distances in a triangle
                double dy = rand.getY() - y;
                //TODO mke sprite change based on shooting direction over walking
                double dirShoot = Math.atan2(dy, dx); // Get angle
                shoot(x, y, dirShoot);
            }

        }
        * */
    }

    private void shootClosest(){
        List<Entity> entities = level.getEntities(this, 500);
        entities.add(level.getClientPlayer()); // add the client player, TODO shouldn't be needed, remove later?

        // Decide what to shoot at
        double min = 0;
        Entity closest = null;
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            double distance = Vector2i.getDistance(new Vector2i(x, y), new Vector2i((int)e.getX(), (int)e.getY())); // TODO remove casts
            if (i == 0 || distance < min){
                min = distance;
                closest = e;
            }
        }
        if (closest != null){
            double dx = closest.getX() - x; // Clac distances in a triangle
            double dy = closest.getY() - y;
            //TODO mke sprite change based on shooting direction over walking
            double dirShoot = Math.atan2(dy, dx); // Get angle
            shoot(x, y, dirShoot);
        }
    }

    public void render(Screen screen) {
        //screen.renderSprite(17 * 16, 53 * 16, new Sprite(80,80, 0xff0000), true);
        Debug.drawRect(screen, 17 * 16, 57 * 16, 100, 40, true);
        // TODO the offset should be based on shooting dir so that shots don't insta collide if mob is hugging wall
        screen.renderMob(x - 16,y -16,this); // -16 as offset to center mob
    }
}
