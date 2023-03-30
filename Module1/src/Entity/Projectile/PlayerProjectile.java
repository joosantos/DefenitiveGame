package Entity.Projectile;
import Entity.Mob.Enemy;
import Graphics.Screen;
import Graphics.Sprite;

import Entity.Spawner.ParticleSpawner;

public class PlayerProjectile extends Projectile{

    public static final int FIRE_RATE = 15; // Higher rate is slower
    private static int DAMAGE;

    public PlayerProjectile(double x, double y, double dir, int damge) {
        super(x, y, dir);
        //range = random.nextInt(50) + 50; // Range between 50 and 100
        range = 100;
        speed = 4;
        DAMAGE = damge;
        //rateOfFire = 15;
        sprite = Sprite.rotate(Sprite.projectile_arrow, angle); // Rotate only once, ep 113
        // Vector maths
        newX = speed * Math.cos(angle); // using vectors to figure out how much to change x and y each tick
        newY = speed * Math.sin(angle); // Math function is giving angle, speed gives travel speed
    }

    //private int time = 0; // part of recurring rotation, ep 113

    public void tick() {
        if (level.tileCollision((int) (x + newX),(int) (y + newY),7, 4, 4)){ //offset is pixels to margin of cell
            level.add(new ParticleSpawner((int)x, (int)y, 50, 10, level, Sprite.particle_normal));
            remove(); //ball projectile thing is 7 by 7
        }else{
             Enemy enemy = level.enemyCollision((int) (x + newX),(int) (y + newY),7, 4, 4);
            if (enemy != null){
                enemy.takeDamage(DAMAGE);
            }
        }

        //TODO If want the projective to rotate as it travels, do this
        /*
        time ++;
        if (time % 6 == 0){
                Sprite.rotate(sprite, Mth.PI / 20.0) or use Sprite.rotate(Sprite.projectile_arrow, newAngle) for rottion on og sprite
        }
        */
        move();

    }

    protected void move(){
        if(!(calculateDistance() > range)){
            x += newX;
            y += newY;
        }else remove();
    }

    private double calculateDistance() {
        double dist = 0;
        dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y))); // Pythagoras theorem
        return dist;
    }

    public void render(Screen screen){
        //-12 and -2 to change point of origin (where char shoots from)
        screen.renderProjectile((int)x - 12, (int)y - 2, this);
    }
}
