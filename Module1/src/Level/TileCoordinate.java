package Level;

public class TileCoordinate {

    private int x, y;
    public static final int TILE_SIZE = 16; //tiles are always 16 pix size

    public TileCoordinate(int x, int y){ //converts tiles to pix
        this.x = x * TILE_SIZE;
        this.y = y * TILE_SIZE;
    }

    public int getY(){
        return y;
    }

    public int getX(){
        return x;
    }

    public int[] xy(){
        int[] r = new int[2];
        r[0] = x;
        r[1] = y;
        return r;
    }
}
