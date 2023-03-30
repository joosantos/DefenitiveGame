package Net.Player;

import Entity.Mob.Mob;
import Entity.Mob.PlayableChar;
import Graphics.Screen;

public class NetPlayer extends PlayableChar {

    public NetPlayer(){
        x = 22*16;
        y = 42*16;
    }

    public void tick() {

    }

    public void render(Screen screen) {
        screen.fillRect(x, y, 32, 32, 0x2030cc, true);
    }

    @Override
    public void takeDamage(int damage) {

    }
}
