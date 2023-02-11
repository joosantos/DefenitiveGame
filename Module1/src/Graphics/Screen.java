package Graphics;

import Entity.Mob.Mob;
import Entity.Mob.Star;
import Entity.Projectile.Projectile;
import Level.Tile.Tile;

import java.util.Arrays;
import java.util.Random;

public class Screen { // This is the class that renders things on the actual screen
    public int width, height;
    public int[] pixels;
    public final int MAP_SIZE = 64;
    public final int MAP_SIZE_MASK = MAP_SIZE-1;
    public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
    public static int ALPHA_COL = 0xFFFF00FF; // remove pink

    public int xOffset = 0;
    public int yOffset = 0;

    private Random random = new Random();

    public Screen(int width, int height){
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];

        for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++){ //64 * 64 = 4096
            tiles[i] = random.nextInt(0xffffff);
        }
    }

    public void clear(){
        Arrays.fill(pixels,0);
        /*for (int i = 0; i < pixels.length; i++){
            pixels[i] = 0;
        }*/
    }

    // For debugging
    public void renderSheet (int xp, int yp, Spritesheet spritesheet, boolean fixed){
        if (fixed){
            xp -= xOffset; // Needed to not render things just in the left corner
            yp -= yOffset;
        }
        for (int y = 0; y < spritesheet.SPRITE_HEIGHT; y++) {
            int ya = y + yp;
            for (int x = 0; x < spritesheet.SPRITE_WIDTH; x++) { //cycling through every pixel of sprite obj
                int xa = x + xp;
                if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
                pixels[xa + ya * width] = spritesheet.pixels[x + y * spritesheet.SPRITE_WIDTH]; // xa and ya to draw on target location
            }
        }
    }

    public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed){ //fixed makes it sit in a part of the map
        if (fixed){
            xp -= xOffset; // Needed to not render things just in the left corner
            yp -= yOffset;
        }
        for (int y = 0; y < sprite.getHeight(); y++) {
            int ya = y + yp;
            for (int x = 0; x < sprite.getWidth(); x++) { //cycling through every pixel of sprite obj
                int xa = x + xp;
                if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
                int col = sprite.pixels[x + y * sprite.getWidth()];
                if (col != ALPHA_COL) pixels[xa + ya * width] = col; // xa and ya to draw on target location
            }
        }

    }

    public void renderTextChar(int xp, int yp, Sprite sprite, int color, boolean fixed){ //fixed makes it sit in a part of the map
        if (fixed){
            xp -= xOffset; // Needed to not render things just in the left corner
            yp -= yOffset;
        }
        for (int y = 0; y < sprite.getHeight(); y++) {
            int ya = y + yp;
            for (int x = 0; x < sprite.getWidth(); x++) { //cycling through every pixel of sprite obj
                int xa = x + xp;
                if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
                int col = sprite.pixels[x + y * sprite.getWidth()];
                if (col != ALPHA_COL) pixels[xa + ya * width] = color; // xa and ya to draw on target location
            }
        }

    }

    public void renderTile(int xp, int yp, Tile tile){ //xp and yp are offsets
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < tile.sprite.SIZE; y++){
            int ya = y + yp;
            for (int x = 0; x < tile.sprite.SIZE; x++){
                int xa = x + xp;
                if (xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) break; // -tile.sprite.SIZE = -16, we're rendering an extra tile
                if (xa < 0) xa = 0;
                pixels[xa + ya * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
            }
        }
    }

    public void renderProjectile(int xp, int yp, Projectile p){
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < p.getSpriteSize(); y++){
            int ya = y + yp;
            for (int x = 0; x < p.getSpriteSize(); x++){
                int xa = x + xp;
                if (xa < -p.getSpriteSize() || xa >= width || ya < 0 || ya >= height ) break;
                if (xa < 0) xa = 0;
                int col = p.getSprite().pixels[x + y * p.getSpriteSize()]; //colour
                if (col != ALPHA_COL) pixels[xa + ya * width] = col; //0xff for 0 opacity
            }
        }
    }
    
    public void renderMob(int xp, int yp, Mob mob){ //swap 32s for 16s if changing 16 pix to 8
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < 32; y++){
            int ya = y + yp;
            int ys = y;
            for (int x = 0; x < 32; x++){
                int xa = x + xp;
                int xs = x;
                if (xa < -16 || xa >= width || ya < 0 || ya >= height) break; // -tile.sprite.SIZE = -16, we're rendering an extra tile
                if (xa < 0) xa = 0;
                int col = mob.getSprite().pixels[xs + ys * 32]; //col stands for colour
                //if ((mob instanceof Chaser) && col == 0xff472bbf) col = 0xffba0015; // change a color in chaser
                if ((mob instanceof Star) && col == 0xff1386AF) col = 0xffe8e83a; // change a color in chaser
                if (col != ALPHA_COL) pixels[xa + ya * width] = col; //need to add an "FF after the 0x for colours"
            }
        }
    }


    public void renderMob(int xp, int yp, Sprite sprite, int flip){ //swap 32s for 16s if changing 16 pix to 8
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < 32; y++){
            int ya = y + yp;
            int ys = y;
            if (flip == 2 || flip == 3) ys = 31 - y; // 2 means flip y, 3 means flip both
            for (int x = 0; x < 32; x++){
                int xa = x + xp;
                int xs = x;
                if (flip == 1 || flip == 3) xs = 31 - x; // 1 means flip x, 3 means flip both
                if (xa < -16 || xa >= width || ya < 0 || ya >= height) break; // -tile.sprite.SIZE = -16, we're rendering an extra tile
                if (xa < 0) xa = 0;
                int col = sprite.pixels[xs + ys * 32]; //col stands for colour
                if (col != ALPHA_COL) pixels[xa + ya * width] = col; //need to add an "FF after the 0x for colours"
            }
        }
    }

    public void setOffset(int xOffset, int yOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void drawRect(int xp, int yp, int width, int height, int color, boolean fixed) {
        if (fixed){
            xp -= xOffset; // Needed to not render things just in the left corner
            yp -= yOffset;
        }
        // xp and yp are the offset
        for (int x = xp; x < xp + width; x++) { // Accounts for xp offset
            if (x < 0 || x >= this.width || yp >= this.height) continue; // Don't render offscreen things
            if (yp > 0) pixels[x + yp * this.width] = color;
            if (yp + height >= this.height) continue; // Check bottom limit
            if (yp + height > 0)pixels[x + (yp + height) * this.width] = color; // Bottom line, if prevents out of bounds
        }

        for (int y = yp; y < yp + height; y++) { // Accounts for yp offset
            if (xp >= this.width || y < 0 || y >= this.height) continue; // Don't render offscreen things
            if (xp > 0) pixels[xp + y * this.width] = color;
            if (xp + width >= this.width) continue; // Check right limit
            if (xp + width > 0)pixels[(xp + width) + y * this.width] = color; // Right side line, if prevents out of bounds
        }
        // Render missing pixel in bot right corner
        if (yp + height >= this.height || xp + width >= this.width || !(xp + width > 0) || !(yp + height > 0)) return; // Check bottom and right limits
        pixels[(xp + width) + (yp + height) * this.width] = color;
        // could have just done y <= yp or x <= xp, but would make extra pixel
    }

    public void fillRect(int xp, int yp, int width, int height, int color, boolean fixed){
        if (fixed){
            xp -= xOffset; // Needed to not render things just in the left corner
            yp -= yOffset;
        }

        for (int y = 0; y < height; y++) {
            int yo = yp + y;
            if (yp < 0 || yo >= this.height) continue;
            for (int x = 0; x < width; x++) {
                int xo = xp + x;
                if (xp < 0 || xo >= this.width) continue;
                pixels[xo + yo * this.width] = color;
            }
        }
    }
}
