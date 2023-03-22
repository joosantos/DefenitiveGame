package Entity.Mob;

import Entity.Projectile.Projectile;
import Entity.Projectile.WizardProjectile;
import Events.Event;
import Events.EventDisptcher;
import Events.EventListener;
import Events.Types.MousePressedEvent;
import Events.Types.MouseReleasedEvent;
import Game.Game;
import Graphics.AnimatedSprite;
import Graphics.Screen;
import Graphics.Sprite;
import Graphics.Spritesheet;
import Graphics.UI.*;
import Input.Keyboard;
import Input.Mouse;
import Utils.ImageUtils;
import Utils.Vector2i;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Player extends Mob implements EventListener {

    private String name;
    private Keyboard input;
    private Sprite sprite;
    boolean walking = false;
    private AnimatedSprite up = new AnimatedSprite(Spritesheet.test_up, 96, 96, 1);
    private AnimatedSprite right = new AnimatedSprite(Spritesheet.test_right, 96, 96, 1);
    private AnimatedSprite down = new AnimatedSprite(Spritesheet.test_down, 96, 96, 1);
    //private AnimatedSprite left = new AnimatedSprite(Spritesheet.player_left, 32, 32, 3);

    private AnimatedSprite animSprite = down; // Holds current animated sprite, down by default

    private int fireRate = 0;// Player got a gun

    private UIManager ui;
    private UIProgressBar uiHealthBar,uiManaBar,uiExperienceBar;
    private UIButton button;
    private UIButton buttonPortal = new UIButton(new Vector2i(10, 400), new Vector2i(40, 30)){               @Override
        public void pressed (UIButton button){
            button.setColor(0xededed);
            button.action(button);
            button.ignoreNextPress();
        }

        @Override
        public void action(UIButton button) {
            System.out.println("Action!");
        }
    };

    private BufferedImage image, imageHover, imagePressed;

    private UIPanel panel = (UIPanel) new UIPanel(new Vector2i((Game.width * Game.scale),0), new Vector2i(360 * Game.scale, Game.width * Game.scale)).setColor(0x4f4f4f);

    private boolean shooting = false;

    @Deprecated
    public Player (String name, Keyboard input){
        this.name = name;
        this.input = input;
        sprite = Sprite.player_forward; //just in case, to avoid crashes
        animSprite = down;
    }

    public Player (String name, int x, int y, Keyboard input){
        this.name = name;
        this.x = x;
        this.y = y;
        this.input = input;
        sprite = Sprite.player_forward; //just in case, to avoid crashes
        fireRate = WizardProjectile.FIRE_RATE;
        // TODO panel was here
        ui = Game.getUIManager();
        // Had a - 80 with width b4 * 3 before made game window and UI separate
        // TODO Changed things here while working on fullscreen, check static nºs and scale
        ui.addPanel(panel);
        // Positions here are dependent on parent position
        // TODO x is 40 to leave room for class sprite
        UILabel nameLabel = new UILabel(new Vector2i(40,200), name);
        nameLabel.setColor(0xbbbbbb);
        nameLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
        nameLabel.dropShadow = true;
        panel.addComponent(nameLabel);
        // The setters returning the object itself allows for these one-liners with a setter at the end


        // XP Bar
        uiExperienceBar = new UIProgressBar(new Vector2i(10,210), new Vector2i(80 * Game.scale- 20,20)); // 80 * 3 is full length, -20for 10 px on each side
        uiExperienceBar.setColor(0x6a6a6a); // 0x5f5f5f is slightly darker
        uiExperienceBar.setForegroundColor(0x32AD34);
        panel.addComponent(uiExperienceBar);
        // XP Label
        UILabel xpLabel = new UILabel(new Vector2i(uiExperienceBar.position).add(new Vector2i(2, 16)), "XP");
        // can't just add 2 cause java starts at bot left (ep 117)
        xpLabel.setColor(0xffffff);
        xpLabel.setFont(new Font("Verdana", Font.PLAIN,17));
        panel.addComponent(xpLabel);

        // HP Bar
        uiHealthBar = new UIProgressBar(new Vector2i(10,235), new Vector2i(80 * Game.scale - 20,20)); // 80 * 3 is full length, -20for 10 px on each side
        uiHealthBar.setColor(0x6a6a6a); // 0x5f5f5f is slightly darker
        uiHealthBar.setForegroundColor(0xdd3030);
        panel.addComponent(uiHealthBar);
        // HP Label
        UILabel hpLabel = new UILabel(new Vector2i(uiHealthBar.position).add(new Vector2i(2, 16)), "HP");
        // can't just add 2 cause java starts at bot left (ep 117)
        hpLabel.setColor(0xffffff);
        hpLabel.setFont(new Font("Verdana", Font.PLAIN,17));
        panel.addComponent(hpLabel);

        // MP Bar
        uiManaBar = new UIProgressBar(new Vector2i(10,260), new Vector2i(80 * Game.scale - 20,20)); // 80 * 3 is full length, -20for 10 px on each side
        uiManaBar.setColor(0x6a6a6a); // 0x5f5f5f is slightly darker
        uiManaBar.setForegroundColor(0xB14CFF); // Some colours make it get a +1 x offset for some reason, avoid blue I guess
        panel.addComponent(uiManaBar);
        // HP Label
        UILabel mpLabel = new UILabel(new Vector2i(uiManaBar.position).add(new Vector2i(2, 16)), "MP");
        // can't just add 2 cause java starts at bot left (ep 117)
        mpLabel.setColor(0xffffff);
        mpLabel.setFont(new Font("Verdana", Font.PLAIN,17));
        panel.addComponent(mpLabel);


        //default health at 100
        this.health = 90;
        this.mana = 80;
        this.experience = 10;

        button = new UIButton(new Vector2i(10, 300), new Vector2i(40, 30)){
            @Override
            public void pressed (UIButton button){
                button.setColor(0xededed);
                button.action(button);
                button.ignoreNextPress();
            }

            @Override
            public void action(UIButton button) {
                System.out.println("Action!");
            }
        };
        button.setText("Hello!");
        panel.addComponent(button);

        // IMG Btn

        try {
            image = ImageIO.read(new File("res/textures/nexus2.png"));
            System.out.println(image.getType());
            } catch (IOException e) {
            e.printStackTrace();
        }

        imageHover = ImageUtils.changedBrightness(image, 50);
        imagePressed = ImageUtils.changedBrightness(image, -50);

        UIButton imageBtn = new UIButton(new Vector2i(10, 380), image){
            @Override
            public void pressed(UIButton button) {
                button.setImage(imagePressed);
                //System.exit(0);
            }

            @Override
            public void released(UIButton button) {
                button.setImage(imageHover);
                System.exit(0);
            }

            @Override
            public void entered(UIButton button) {
                button.setImage(imageHover);
            }

            @Override
            public void exited(UIButton button) {
                button.setImage(image);
            }
        };

        panel.addComponent(imageBtn);
    }

    public void onEvent(Event event){
        EventDisptcher dispatcher = new EventDisptcher(event);
        // Same as writting an inner class
        dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onMousePressed ((MousePressedEvent) e));
        dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onMouseReleased ((MouseReleasedEvent) e));
    }

    public void tick(){
        if (walking)animSprite.tick(); // run animation
        else animSprite.setFrame(0);
        if (fireRate > 0) fireRate--;
        double xa = 0, ya = 0;
        double speed = 1.0;
        if (input.left){
            xa -= speed;
            //xa--;
            animSprite = right; // Still have the flip thing to render it backwards
        }
        if (input.right){
            xa += speed;
            //xa++;
            animSprite = right;
        }
        if (input.up) {
            ya -= speed;
            //ya--;
            animSprite = up;
        }
        if (input.down){
            ya += speed;
            //ya++;
            animSprite = down;
        }

        if (xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        }else walking = false;

        clear();
        updateShooting();

        uiHealthBar.setProgress(health / 100.0);
        uiManaBar.setProgress(mana / 100.0);
        uiExperienceBar.setProgress(experience / 100.0);
    }

    private void updateShooting(){
        if (!shooting || fireRate > 0) return;
        double dx = Mouse.getX() - Game.getWindowWidth()/2;// player position on screen is always center
        double dy = Mouse.getY() - Game.getWindowHeight()/2;// so divide screen size by 2 to get center
        double dir = Math.atan2(dy, dx); //atan2() receives y first, atan2 doesn't divide by 0, atan does
        shoot(x,y, dir); //x nd y are player position(where projectile originates)
        fireRate = WizardProjectile.FIRE_RATE; // Reset fire rate after shooting

    }

    public boolean onMousePressed (MousePressedEvent e){ // TODO Fix dir, bigger screen fucks it
        // Direction of shooting (using arc tan and triangle maths to figure angle out, we know all side of triangle)
        if (e.getButton() == MouseEvent.BUTTON1 && fireRate <= 0){ // 1 is left mouse click, w8 for permission to shoot
            shooting = true;
            double dx = Mouse.getX() - Game.getWindowWidth()/2;// player position on screen is always center
            double dy = Mouse.getY() - Game.getWindowHeight()/2;// so divide screen size by 2 to get center
            double dir = Math.atan2(dy, dx); //atan2() receives y first, atan2 doesn't divide by 0, atan does
            shoot(x,y, dir); //x nd y are player position(where projectile originates)
            fireRate = WizardProjectile.FIRE_RATE; // Reset fire rate after shooting
            return true; // true means it's been handled, others won't do it
        }
        return false;
    }

    public boolean onMouseReleased (MouseReleasedEvent e){
        if (e.getButton() == MouseEvent.BUTTON1){
            shooting = false;
            return true;
        }
            return false;
    }

    private void clear() {
        for (int i = 0; i < level.getProjectiles().size(); i++) {
            Projectile p = level.getProjectiles().get(i);
            if (p.isRemoved()) level.getProjectiles().remove(i);
        }
    }

    public String getName() {
        return name;
    }


    public void render(Screen screen){
        int flip = 0;
        if (dir == Direction.LEFT) flip = 1; // for flipping right sprite into left

        sprite = animSprite.getSprite(); // new animation

        screen.renderMob((x - 16),(y - 16), sprite, flip, sprite.getHeight());//16 is half the size of the player, centering them

        //Rendering 4 diff things isn't slower

        /*int xx = x - 16;//to center char
        int yy = y - 16;//to center char
        screen.renderPlayer(xx,yy, Sprite.player0);
        screen.renderPlayer(xx + 16,yy, Sprite.player1);
        screen.renderPlayer(xx,yy + 16, Sprite.player2);
        screen.renderPlayer(xx + 16,yy + 16, Sprite.player3);*/

    }


    protected void move(double xa, double ya){
        if (ya > 0) dir = Direction.UP;
        else if (ya < 0) dir = Direction.DOWN;
        else if (xa > 0) dir = Direction.RIGHT; //swapping 1 and 3 to bottom gives priority to horizontal sprites
        else if (xa < 0) dir = Direction.LEFT;

        while (xa != 0){
            if (Math.abs(xa) >= 1){ // keeps decimals to add up
                if (!collision(abs(xa),ya)){
                    this.x += abs(xa); // allows faster movement with proper collision
                }
                xa -= abs(xa);
            }else{ // Add decimals that will not increment instantly
                if (!collision(abs(xa),ya)){
                    this.x += xa; // allows faster movement with proper collision
                }
                xa = 0;
            }
        }

        while (ya != 0){
            if (Math.abs(ya) >= 1){ // keeps decimals to add up
                if (!collision(xa,abs(ya))){
                    this.y += abs(ya); // allows faster movement with proper collision
                }
                ya -= abs(ya);
            }else{ // Add decimals that will not increment instantly
                if (!collision(xa,abs(ya))){
                    this.y += ya; // allows faster movement with proper collision
                }
                ya = 0;
            }

        }
    }

    private boolean collision(double xa, double ya){
        //check each corner of tile
        for (int c = 0; c < 4; c++){ //check current location + movement, divide by 16 to convert from pix to tiles
            double xt = ((x + xa) - c % 2 * 15) / 16;// can't do bitwise operations on doubles ( >> 4)
            double yt = ((y + ya) - c / 2 * 15) / 16; // TODO changed 2 * 16 to 2 * 15 in ep102 for mob not getting stuck in corners
            // Right side corners
            int ix = (int) Math.ceil(xt); // Rounds the number up with Math.ceil
            int iy = (int) Math.ceil(yt);
            if ( c % 2 == 0) ix = (int) Math.floor(xt); // Right side needs diff rounding
            if ( c / 2 == 0) iy = (int) Math.floor(yt); // Same for upside
            if(level.getTile(ix, iy).isSolid()) return true;
            if(level.getTile(ix, iy).isPortal()){
                buttonPortal.setText("Enter");
                panel.addComponent(buttonPortal);
            }else{
                panel.removeComponent(buttonPortal);
                panel.removeComponent(buttonPortal.getLabel());
            }
        }
        /*
        Changing 2(14) to a larger number increases the width, changing -1(-8) positions the collision box.
        Play with the code, first try it at 0. You'll see the collision zone is just a line.
        Increase it to 10 and it'll be about the size of a tile (depends on your sprites).
        You'll see that the box is the right size, but it's shifted to the left,
        use the second number to shift it right and then tweak the numbers until it's perfect.
        */
        return false;
    }
}
