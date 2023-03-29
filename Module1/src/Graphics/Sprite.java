package Graphics;

public class Sprite {

    public final int SIZE;
    private int x, y;
    public int[] pixels;
    protected Spritesheet sheet;
    private int width, height;


    //Generic tile sprites

    public static Sprite grass = new Sprite(16, 0, 0, Spritesheet.tiles); //creating the sprite, loads the first cell (cause coordenates are 0,0 and size is 16) ((0,1) gives second row first cell)
    public static Sprite flower = new Sprite(16, 0, 1, Spritesheet.tiles); //creating the sprite, loads the first cell (cause coordenates are 0,0 and size is 16) ((0,1) gives second row first cell)
    public static Sprite rock = new Sprite(16, 0, 2, Spritesheet.tiles); //creating the sprite, loads the first cell (cause coordenates are 0,0 and size is 16) ((0,1) gives second row first cell)
    public static Sprite voidSprite = new Sprite(16, 0x1B87E0); //0 is black; white is 0xffffff; try colorpicker.com

    //Spawn level sprites:

    public static Sprite spawn_grass = new Sprite(16,0,0, Spritesheet.spawn_level_tiles);
    public static Sprite spawn_hedge = new Sprite(16,1,0, Spritesheet.spawn_level_tiles);
    public static Sprite spawn_water = new Sprite(16,2,0, Spritesheet.spawn_level_tiles);
    public static Sprite spawn_wall1 = new Sprite(16,0,1, Spritesheet.spawn_level_tiles);
    public static Sprite spawn_wall2 = new Sprite(16,0,2, Spritesheet.spawn_level_tiles);
    public static Sprite spawn_floor_boards = new Sprite(16,1,2, Spritesheet.spawn_level_tiles);
    public static Sprite spawn_floor = new Sprite(16,1,1, Spritesheet.spawn_level_tiles);//My own addition

    public static Sprite spawn_portal = new Sprite(16,2,1, Spritesheet.spawn_level_tiles);//My own addition




    //Player sprites:

    /* 8 pix player (only backside)
    public static Sprite player0 = new Sprite(16, 8, 10, Spritesheet.tiles);
    public static Sprite player1 = new Sprite(16, 9, 10, Spritesheet.tiles);
    public static Sprite player2 = new Sprite(16, 8, 11, Spritesheet.tiles);
    public static Sprite player3 = new Sprite(16, 9, 11, Spritesheet.tiles);
    */

    //16 pix player
    //public static Sprite player_left = new Sprite(32, 1, 0, Spritesheet.mob); //each unit is 4 blocks (2 by 2)
    //public static Sprite player_right = new Sprite(32, 0, 0, Spritesheet.mob); //each unit is 4 blocks (2 by 2)
    //public static Sprite player_back = new Sprite(32, 0, 1, Spritesheet.mob); //each unit is 4 blocks (2 by 2)
    public static Sprite player_forward = new Sprite(32, 0, 0, Spritesheet.player); //each unit is 4 blocks (2 by 2)
    public static Sprite mob_forward = new Sprite(32, 1, 1, Spritesheet.mob); //each unit is 4 blocks (2 by 2)

    //public static Sprite Player_forward_1 = new Sprite(32, 4, 6, Spritesheet.tiles);
    //public static Sprite Player_forward_2 = new Sprite(32, 4, 7, Spritesheet.tiles);

    //public static Sprite Player_side_1 = new Sprite(32, 5, 6, Spritesheet.tiles);
    //public static Sprite Player_side_2 = new Sprite(32, 5, 7, Spritesheet.tiles);

    //public static Sprite Player_back_1 = new Sprite(32, 6, 6, Spritesheet.tiles);
    //public static Sprite Player_back_2 = new Sprite(32, 6, 7, Spritesheet.tiles);*/

    //Projectile sprites
    public static Sprite projectile_wizard = new Sprite(16,0,0, Spritesheet.projectile_wizard);
    public static Sprite projectile_arrow = new Sprite(16,1,0, Spritesheet.projectile_wizard);
    public static Sprite projectile_arrow2 = new Sprite(16,2,0, Spritesheet.projectile_wizard);

    //Particles
    public static Sprite particle_normal = new Sprite(3, 0xaaaaaa);
    public static Sprite particle_red = new Sprite(3, 0xff0000);


    protected Sprite(Spritesheet sheet, int width, int height){ // For AnimatedSprite
        SIZE = (width == height) ? width : -1; // Size = width if square else Size = -1
        this.width = width;
        this.height = height;
        this.sheet = sheet;
    }

    public Sprite(int size, int x, int y, Spritesheet sheet){
        SIZE = size;
        this.width = size;
        this.height = size;
        pixels = new int[SIZE * SIZE];
        this.x = x * size; //Setting location
        this.y = y * size; //Setting location
        this.sheet = sheet;
        load();
    }

    public Sprite (int width, int height, int col){ // For non square sprite (ep 75), use for UI
        SIZE = -1;
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        setColour(col);
    }

    public Sprite(int size, int colour){
        SIZE = size;
        this.width = size;
        this.height = size;
        pixels = new int[SIZE * SIZE];
        setColour(colour);
    }

    public Sprite(int[] pixels, int width, int height){
        SIZE = (width == height) ? width : -1;
        this.width = width;
        this.height = height;
        // Forcing it to copy instead of passing reference
        this.pixels = pixels;
        // This code isn't needed after putting pixels inside the loops
        //this.pixels = new int[pixels.length]; // Necessary so they don't always point towards the same pixels (ep 109)
        //System.arraycopy(pixels, 0, this.pixels, 0, pixels.length); // This
        // Does the same as this:
        /*for (int i = 0; i < pixels.length; i++) {
            this.pixels[i] = pixels[i];
        }*/

    }

    public static Sprite rotate (Sprite sprite, double angle){
        return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
    }


    private static int[] rotate (int[] pixels, int width, int height, double angle){ // ep 112
        int[] result = new int[width*height];

        double nx_x = rotateX(-angle, 1.0, 0.0); // The 0.0 cancels out the y
        double nx_y = rotateY(-angle, 1.0, 0.0);
        double ny_x = rotateX(-angle, 0.0, 1.0);// then the x
        double ny_y = rotateY(-angle, 0.0, 1.0);

        double x0 = rotateX(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
        double y0 = rotateY(-angle, -width / 2.0, -height / 2.0) + height / 2.0;

        for (int y = 0; y < height; y++) {
            double x1 = x0;
            double y1 = y0;
            for (int x = 0; x < width; x++) {
                int xx = (int) x1;
                int yy = (int) y1;
                int col = 0;
                if (xx < 0 || xx >= width || yy < 0 || yy >= height) col = Screen.ALPHA_COL; // check if out of bounds, no white parts
                    // copying the pixel from old position in old array to new position in new array
                else col = pixels[xx + yy * width];
                result[x + y * width] = col;
                x1 += nx_x;
                y1 += nx_y;
            }
            x0 += ny_x;
            y0 += ny_y;
        }

        return result;
    }

    // These two calculate the new absolute position
    private static double rotateX (double angle, double x, double y){
        double cos = Math.cos(angle - Math.PI / 2); // - Math.PI / 2 to rotate it 90º to left, cause drew pointing up
        double sin = Math.sin(angle - Math.PI / 2);
        return x * cos + y * -sin;
    }

    private static double rotateY (double angle, double x, double y){
        double cos = Math.cos(angle - Math.PI / 2);
        double sin = Math.sin(angle - Math.PI / 2);
        return x * sin + y * cos;
    }

    public static Sprite[] split(Spritesheet sheet) {
        // () are there for ease of reading
        int amount = (sheet.getWidth() * sheet.getHeight() / (sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT));
        Sprite[] sprites = new Sprite[amount];
        int current = 0; // Current sprite we're working with

        for (int yp = 0; yp < sheet.getHeight() / sheet.SPRITE_HEIGHT; yp++) {
            for (int xp = 0; xp < sheet.getWidth() / sheet.SPRITE_WIDTH; xp++) {
                // Placing pixels inside the loops results in it not always being the last sprite added
                int[] pixels = new int[sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT];

                for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
                    for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
                        int xo = x + xp * sheet.SPRITE_WIDTH; //offset
                        int yo = y + yp * sheet.SPRITE_HEIGHT; //offset
                        // Find the current pixel we're reading from the sprite sheet
                        pixels[x+y*sheet.SPRITE_WIDTH] = sheet.getPixels()[xo + yo * sheet.getWidth()];
                    }
                }
                // Add sprite to list, then increment current
                sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
            }
        }

        return sprites;
    }

    private void setColour(int colour) {
        for (int i = 0; i < width * height; i++){
            pixels[i] = colour;
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    private void load(){ //Extracts a single sprite from the sheet
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SPRITE_WIDTH];
            }
        }
    }

}
