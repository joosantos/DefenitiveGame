package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Spritesheet {

    private String path;
    public final int SIZE; // Size of each sprite (16)
    public final int SPRITE_WIDTH, SPRITE_HEIGHT;  // Size of each sprite (16)
    private int width, height; // Size of the sprite sheet
    public int[] pixels;
    //Testing
    //public static Spritesheet medusa = new Spritesheet("res/textures/sheets/medusa.png", 92);//"/textures/spritesheet.png", 256);
    public static Spritesheet mob = new Spritesheet("res/textures/sheets/archer.png", 256);//"/textures/spritesheet.png", 256);

    public static Spritesheet tiles = new Spritesheet("res/textures/sheets/spritesheet.png", 256);//"/textures/spritesheet.png", 256);
    public static Spritesheet spawn_level_tiles = new Spritesheet("res/textures/sheets/spawnLevelSheet.png", 48);
    public static Spritesheet projectile_wizard = new Spritesheet("res/textures/sheets/projectiles/wizard.png", 48);

    // New player spite loading
    public static Spritesheet player = new Spritesheet("res/textures/sheets/playerSheet.png", 128, 96);
    public static Spritesheet player_up = new Spritesheet(player, 0, 0, 1, 3, 32);
    public static Spritesheet player_right = new Spritesheet(player, 1, 0, 1, 3, 32);
    public static Spritesheet player_down = new Spritesheet(player, 2, 0, 1, 3, 32);
    public static Spritesheet player_left = new Spritesheet(player, 3, 0, 1, 3, 32);

    public static Spritesheet dummy = new Spritesheet("res/textures/sheets/archer.png", 256, 256);
    public static Spritesheet dummy_up = new Spritesheet(dummy, 0, 1, 1, 1, 32);
    public static Spritesheet dummy_right = new Spritesheet(dummy, 0, 0, 1, 1, 32);
    public static Spritesheet dummy_down = new Spritesheet(dummy, 1, 1, 1, 1, 32);
    public static Spritesheet dummy_left = new Spritesheet(dummy, 1, 0, 1, 1, 32);

    private Sprite[] sprites;

    // Extract sprites from sprite sheet
    public Spritesheet(Spritesheet sheet, int x, int y, int width, int height, int spriteSize){ // pixel precision
        int xx = x * spriteSize; // Sprite precision
        int yy = y * spriteSize;
        int w = width * spriteSize; // w and h are pixel precision
        int h = height * spriteSize;
        if (width == height) SIZE = width;
        else SIZE = -1;
        SPRITE_WIDTH = w;
        SPRITE_HEIGHT = h;
        pixels = new int[w * h];
        for (int y0 = 0; y0 < h; y0++) { // Sprite precision for width and height
            int yp = yy + y0; // Y position
            for (int x0 = 0; x0 < w; x0++) {
                int xp = xx + x0; // X position
                pixels[x0 + y0 * w] = sheet.pixels[xp + yp * sheet.SPRITE_WIDTH];
            }
        }
        int frame = 0;
        sprites = new Sprite[width * height];
        for (int ya = 0; ya < height; ya++) {
            for (int xa = 0; xa < width; xa++) {
                int[] spritePixels = new int[spriteSize * spriteSize];
                for (int y0 = 0; y0 < spriteSize; y0++) {
                    for (int x0 = 0; x0 < spriteSize; x0++) {
                        // x0 is pixel precision, but xa is sprite precision, so multiply by spriteSize
                        spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize + (y0 + ya * spriteSize) * SPRITE_WIDTH)];
                    }
                }
                Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
                sprites[frame++] = sprite; // Increments after
            }
        }

        //pixels = new int[SIZE * SIZE];
        //load();
    }

    public Spritesheet(String path, int size){
        this.path = path;
        SIZE = size;
        SPRITE_WIDTH = size;
        SPRITE_HEIGHT = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    public Spritesheet(String path, int width, int height){
        this.path = path;
        SIZE = -1;
        SPRITE_WIDTH = width;
        SPRITE_HEIGHT = height;
        pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
        load();
    }

    // Extract single sprite from sheet
    public Sprite[] getSprites(){
        return sprites;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }

    private void load(){
        try { // End of ep 108 has printlns, but don't work with this method of reading (cause delay)
            BufferedImage image = ImageIO.read( new File(path) );
            // BufferedImage image = ImageIO.read(
            // ); //Reads the buffered image onto image
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height]; // TODO wasn't using this, not necessary?
            image.getRGB(0,0,width,height,pixels,0,width); //break the buffered image into pixels, stored in pixels
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
