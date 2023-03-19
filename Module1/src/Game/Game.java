package Game;

import Entity.Mob.Player;
import Events.Event;
import Events.EventListener;
import Graphics.Layers.Layer;
import Graphics.Screen;
import Input.Keyboard;
import Input.Mouse;
import Level.Level;
import Level.TileCoordinate;
import Graphics.UI.UIManager;
import Net.Client;
import Net.Player.NetPlayer;
import com.thecherno.raincloud.serialization.RCDatabase;
import com.thecherno.raincloud.serialization.RCField;
import com.thecherno.raincloud.serialization.RCObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements Runnable, EventListener { //runnable makes it so that the thread with "this" executes the "run" function
    private static final long serialVersionUID = 1L; //optional convention of Java

    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static int width = (int) (screenSize.getWidth() - 360); //was 300;
    public static int height = (int) screenSize.getHeight();//300 / 16 * 9;
    public static int scale = 1;
    private static String title = "ROTMG";

    private Thread thread;
    private JFrame frame;
    private Keyboard key;
    private Level level;
    private Player player;
    private boolean running = false;

    private static UIManager uiManager;

    private Screen screen;
    private BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
    // Creates a reference to the image, if og gets changed, so gets pixels
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); //Allows things to be drawn on image
    // In java arrays and objects are references, unlike the rest of variables

    private List<Layer> layerStack = new ArrayList<Layer>();// Layer stack

    public Game() {
        // TODO If want to change UI, +80*3 here, which adds room for it, and the thing In Player
        // Nothing is being rendered under UI, gud for performance
        Dimension size = new Dimension(width * scale + 80 * scale, height * scale);
        setPreferredSize(size);

        screen = new Screen(width,height);
        uiManager = new UIManager();
        frame = new JFrame();
        key = new Keyboard();

        //TODO Server connection here!

        Client client = new Client("localhost", 8192);
        if(!client.connect()){
            //TODO handle failed connection
        }

        RCDatabase db = RCDatabase.DeserializeFromFile("res/data/screen.bit");
        client.send(db);

        //level = new RandomLevel(64,64);
        level = Level.spawn; //holds the path
        addLayer(level); // add it to layer list to get it auto updated with rest
        TileCoordinate playerSpawn = new TileCoordinate(19,57);
        player = new Player("DudeWat",playerSpawn.getX(),playerSpawn.getY(),key); //params are spawn coordinates, 16 pix = 1 tile (hence the multiplication)

        //player.init(level);
        level.add(player);
        level.addPlayer(new NetPlayer());

        addKeyListener(key);

        Mouse mouse = new Mouse(this); //gon create it here since all its methods are static, so there's no point placing outside(?)
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        save();
    }

    private void save(){
        RCDatabase db = new RCDatabase("Screen");

        RCObject obj = new RCObject("Resolution");
        obj.addField(RCField.Integer("width",width));
        obj.addField(RCField.Integer("height",height));
        db.addObject(obj);

        db.serializeToFile("res/data/screen.bit");
    }

    //window is in name cause canvas already has a getWidth() method
    public static int getWindowWidth(){return width * scale;}

    public static int getWindowHeight(){
        return height * scale;
    }

    public static UIManager getUIManager() {
        return uiManager;
    }

    public void addLayer(Layer layer){layerStack.add(layer);}

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0;
        int ticks = 0;
        requestFocus(); //to skip having to click window, also keeps shooting from stopping movement(maybe, not rly)
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1 ){
                tick(); //update gated by real time
                ticks++;
                delta--;
            }
            render(); //unlimited for fps purposes
            frames++;

            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                frame.setTitle(Game.title + "  |  " + ticks + " ticks, " + frames + " fps"); //Adds fps to title
                ticks = 0;
                frames = 0;
            }
        }
    }

    public void onEvent(Event event){
        for (int i = layerStack.size() - 1; i >= 0 ; i--) { // Go through layerStack in reverse order (1st to last)
            layerStack.get(i).onEvent(event);
        }
    }

    public void tick(){
        key.tick();
        uiManager.tick();

        // Update layers here:
        for (int i = 0; i < layerStack.size(); i++) {
            layerStack.get(i).tick();
        }
    }

    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if (bs == null){
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        double xScroll = player.getX() - screen.width /2; //centering screen on player
        double yScroll = player.getY() - screen.height /2;
        level.setScroll((int) xScroll,(int) yScroll);
        //level.render(screen); Swapped out for rendering of layers

        // Render layers here:
        for (int i = 0; i < layerStack.size(); i++) {
            layerStack.get(i).render(screen);
        }

        /*
        // TODO Wild particles
        Sprite sprite2 = new Sprite(2, 2, 0xffffff);
        Random random = new Random();
        for (int i = 0; i < 100; i++) { //Render 100 2by2 particles
            int x = random.nextInt(20);
            int y = random.nextInt(20);
            screen.renderSprite(width - 60 + x, 50 + y, sprite2, true);
        }*/


        for (int i = 0; i < pixels.length; i++){ // TODO replace with systemArray.copy()?
            pixels[i] = screen.pixels[i]; // TODO do  - 50 or + 50 to each pixel for cool effects, ep 115
        }

        Graphics g = bs.getDrawGraphics();
        // TODO check out ep115 and ep116 for more insight on high resolution


        g.drawImage(image, 0, 0, getWindowWidth(), getWindowHeight(), null);
        //g.drawImage(image, 0, 0, width, height, null); // TODO this loads at true size
        uiManager.render(g); // Comment this out to check if rendering under UI
        //g.setColor(Color.WHITE);
        //g.setFont(new Font("Verdana", 0, 50));
        //g.fillRect(Mouse.getX() - 32, Mouse.getY() - 32, 64, 64); //draw mouse, subtract half of size from coordinates for centering
        g.dispose();
        bs.show();
    }


    public static void main(String[] args) {
        Game game = new Game();
        // Move the undecorated line further down for gratuitous errors; it removes header on window
        game.frame.setUndecorated(true);
        game.frame.setResizable(false);
        game.frame.setTitle(Game.title);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null); //game.frame.setLocation(null);
        //

        game.frame.setVisible(true);

        //This makes it full screen
        game.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setVisible(true);


        game.start();
        //game.requestFocus(); //to skip having to click window

    }
}
