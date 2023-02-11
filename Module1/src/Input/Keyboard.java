package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    private boolean[] keys = new boolean[120];//boolean[65536];
    public boolean up, down, left, right;


    public void tick(){
        up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
        right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
        left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
        down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
