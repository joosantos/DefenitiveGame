package Level;

import Utils.Vector2i;

public class Node { // For path finding

    public Vector2i tile;
    public Node parent;
    public double fCost, gCost, hCost;

    public Node(Vector2i tile, Node parent, double gCost, double hCost) {
        this.tile = tile;
        this.parent = parent;
        this.gCost = gCost; // Sum of all node to node distances (diagonal is higher)
        this.hCost = hCost; // Cost of current node to finish in a str8 line (our guess)
        this.fCost = this.gCost + this.hCost; // total cost
    }
}
