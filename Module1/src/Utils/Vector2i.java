package Utils;

public class Vector2i { // Not  good ide to use too mny of this, hurts performance by allocating the on the heap

    public int x, y; // TODO Made these public, remove ll usages of getX and getY

    public Vector2i (int x, int y){
        set(x,y);
    }

    public Vector2i (Vector2i vector){
        set(vector.x, vector.y);
    }

    public Vector2i (){
        set(0,0);
    }

    public Vector2i add (Vector2i vector){
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public Vector2i add (int value){
        this.x += value;
        this.y += value;
        return this;
    }

    public Vector2i subtract (Vector2i vector){
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    // If setters return class type they can be called with constructor aswell
    public Vector2i setX(int x) {
        this.x = x;
        return this;
    }
    // Like: Vector2i location = new Vector2i(80.40).setY(20);
    public Vector2i setY(int y) {
        this.y = y;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static double getDistance (Vector2i v0, Vector2i v1){
        double distance = 0;
        double x = v0.getX() - v1.getX();
        double y = v0.getY() - v1.getY();
        return Math.sqrt(x*x + y*y); //Pythagoras
    }

    public boolean equals(Object object){ // Override the equals method for Vectors (all objs hve it in jva)
        if (!(object instanceof Vector2i)) return false;
        Vector2i vec = (Vector2i) object;
        if (vec.getX() == this.getX() && vec.getY() == this.getY()) return true; // Compare them
        return false;
    }

}
