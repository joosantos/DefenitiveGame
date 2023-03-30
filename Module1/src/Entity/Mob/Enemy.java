package Entity.Mob;
import Entity.Projectile.EnemyProjectile;
import Entity.Projectile.PlayerProjectile;
import Entity.Projectile.Projectile;
import Game.Game;
import Graphics.Screen;
import Input.Mouse;

public class Enemy extends Mob{

    protected int fireRate = 0;
    protected boolean shooting = false;

    public Enemy() {
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Screen screen) {

    }

    @Override
    public void takeDamage(int damage) {

    }

    @Override
    protected void shoot(int x, int y, double dir) {
        //dir *= 180 /Math.PI; //convert from Rads to Degrees
        Projectile p = new EnemyProjectile(x,y,dir);
        level.add(p);
    }

}
