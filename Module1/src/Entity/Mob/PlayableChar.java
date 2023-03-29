package Entity.Mob;

import Entity.Projectile.PlayerProjectile;
import Entity.Projectile.Projectile;
import Graphics.Screen;

public class PlayableChar extends Mob{ //TODO look at this for things both classes need
    @Override
    public void tick() {

    }

    @Override
    public void render(Screen screen) {

    }

    @Override
    public void takeDamage() {

    }

    protected void shoot(int x, int y, double dir){
        //dir *= 180 /Math.PI; //convert from Rads to Degrees
        Projectile p = new PlayerProjectile(x,y,dir);
        level.add(p);
    }
}
