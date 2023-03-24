package Level;

import Entity.Entity;
import Entity.Mob.Mob;
import Entity.Mob.Player;
import Entity.Particle.Particle;
import Entity.Projectile.Projectile;
import Events.Event;
import Graphics.Layers.Layer;
import Graphics.Screen;
import Level.Tile.Tile;
import Utils.Vector2i;

import java.util.*;

public class Level extends Layer { // Level is a layer, UI is another(?)
    protected int width, height;
    protected int[] tilesInt;
    protected int[] tiles;
    public static Level spawn = new SpawnLevel("res/levels/spawnLevel.png");
    @Deprecated public static Level random = new RandomLevel(64,64);
    public static Level level1 = new SpawnLevel("res/levels/level1.png");

    private int xScroll, yScroll;

    private List<Entity> entities = new ArrayList<Entity>(); //Holds all entities of the level (dynamically)
    private List<Projectile> projectiles = new ArrayList<Projectile>(); //Holds all entities of the level (dynamically)
    private List<Particle> particles = new ArrayList<Particle>(); //Holds all particles of the level (dynamically)

    private List<Mob> players = new ArrayList<Mob>();

    private Comparator<Node> nodeSorter = new Comparator<Node>() {
        public int compare(Node n0, Node n1) {
            if (n1.fCost < n0.fCost) return +1; // positive 1
            if (n1.fCost > n0.fCost) return -1; // negative 1
            return 0;
        }
    };

    public Level (int width, int height){
        this.width = width;
        this.height = height;
        tilesInt = new int [width * height];
        generateRandomLevel();
    }

    public Level (String path){
        loadLevel(path);
        generateRandomLevel();
        //Adding Spawner in the constructor before the level finishes being instanced leads to crash
        //add(new Spawner(16*16, 16* 57, Spawner.Type.PARTICLE, 50, this)); //Use Level in constructor to get around this
    }

    protected void generateRandomLevel() {
         
    }

    protected void loadLevel(String path) {

    }

    protected void generateLevel(){

    }


    private void time(){ //manage time for day/night cycles in levels

    }


    public void onEvent(Event event){
        getClientPlayer().onEvent(event); // Client player should be the only one raising events in multiplayer scenario
    }

    public void tick(){ //Tick all Entities in our arrays
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).tick();
            if (entities.get(i).isRemoved()) entities.remove(i); // When it is removed from array list java will auto delete it
        }
        for (int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).tick();
            if(projectiles.get(i).isRemoved()) projectiles.remove(i);
        }
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).tick();
            if(particles.get(i).isRemoved()) particles.remove(i);
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).tick();
            if(players.get(i).isRemoved()) players.remove(i);
        }
        // TODO Not rly, but he made a remove function called here that runs through everything again and checks isRemoved, maybe current method messes up indexes
    }

    public void setScroll (int xScroll, int yScroll){
        this.xScroll = xScroll;
        this.yScroll = yScroll;
    }

    public void render (Screen screen){
        screen.setOffset(xScroll,yScroll); // Every time level is rendered, offset is updated, moving things with the map
        int x0 = xScroll >> 4;// by size of tiles to tile precision
        int x1 = (xScroll + screen.width + 16) >> 4; //+16 to render extra tile
        int y0 = yScroll >> 4;
        int y1 = (yScroll + screen.width + 16) >> 4; //+16 to render extra tile

        for(int y = y0; y < y1; y++){
            for(int x = x0; x < x1; x++) {
                if (x + y * width < 0 || x + y * width >= width * height ){//fill out of bounds, 256 = 16^2
                    Tile.voidTile.render(x, y, screen);
                    continue;
                }

                getTile(x,y).render(x,y,screen);
                //tiles[x + y * 16].render(x, y, screen); //since we have it on tile precision, we just use x and y
            }
            // Rendering all our Entities
            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).render(screen);
            }
            for (int i = 0; i < projectiles.size(); i++) {
                projectiles.get(i).render(screen);
            }
            for (int i = 0; i < particles.size(); i++) {
                particles.get(i).render(screen);
            }
            for (int i = 0; i < players.size(); i++) {
                players.get(i).render(screen);
            }
        }
    }

    // offsets are for sprites that are centered (not occupying full cell)
    public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset){
        for (int c = 0; c < 4; c++){ // Checks 4 corners
            int xt = (x - c % 2 * size + xOffset) >> 4;// c % 2 will alternate between 0 and 1 depending on corner
            int yt = (y - c / 2 * size + yOffset) >> 4;// >> 4 = / 16, but faster
            if(getTile(xt, yt).isSolid()) return true;
        }
        return false;
    }


    public void add(Entity e){  // Load Entities into the level
        e.init(this);
        if (e instanceof Particle) particles.add((Particle) e); //if adding particles, need to cast as a Particle to be safe
        else if(e instanceof Projectile) projectiles.add((Projectile)e); //Need to cast to Projectile to be safe (silence errors)
        else if (e instanceof Player) players.add((Player) e);
        else entities.add(e);
    }

    public List<Projectile> getProjectiles(){
        return projectiles;
    }

    public void addPlayer(Mob player){
        player.init(this);
        players.add(player);
    }

    public List<Mob> getPlayers(){ // TODO Put players in own list instead of using Mob, make an intermediate player class that brings online and client together
        return players;
    }

    public Mob getPlayerAt(int index){
        return players.get(index);
    }

    public Player getClientPlayer(){
        // No matter how many players are in the game, the first one on the list is the player of the person playing
        return (Player) players.get(0);
    }

    // A* search algorithm - ep 100
    public List<Node> findPath (Vector2i start, Vector2i goal){
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node current = new Node(start,null, 0, getDistance(start,goal));
        openList.add(current);
        while (openList.size() > 0){
            Collections.sort(openList, nodeSorter); // Move Node based on nodeSorter (arrange indexes by shortest path)
            current = openList.get(0); // Consider lowest cost first
            if (current.tile.equals(goal)){
                List<Node> path = new ArrayList<Node>();
                while (current.parent != null){ // trace steps from finish to start
                    path.add(current);
                    current = current.parent;
                }
                openList.clear();
                closedList.clear();
                return path;
            }
            openList.remove(current);
            closedList.add(current);
            for (int i = 0; i < 9; i++) {
                if (i == 4) continue; // 4 is current tile(node) (aka center)
                int x = current.tile.getX();
                int y = current.tile.getY();
                int xi = (i % 3) -1; // 4 is (0,0)
                int yi = (i / 3) -1; // These gives position of neighbours in form of (0,-1) or (1,1) relative to 4
                Tile at = getTile(x + xi, y + yi);
                if (at == null) continue;
                if (at.isSolid()) continue; // Don't walk into walls
                Vector2i a = new Vector2i(x + xi, y + yi); // at, but in vector form
                // The if check makes it prefer early diagonals
                double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95); // calc distance of neighbour, diagonal is bigger
                double hCost = getDistance(a, goal); // Guessed cost
                Node node = new Node(a, current, gCost, hCost); // Parent is where we were lsat, aka current
                // Will skip previously considered nodes unless they re viable again(2º condition)
                if (vectorInList(closedList, a) && gCost >= current.gCost) continue; // TODO replaced node with current, check if correct in ep 101
                if (!vectorInList(openList, a) || gCost < current.gCost) openList.add(node); // Don't add dupes

            }
        }
        closedList.clear();
        // The only way we get here is if open list's size is 0, so no point clearing it
        return null; // Nothing found
    }

    // Check if vector is in List
    private boolean vectorInList(List<Node> list, Vector2i vector){
        for (Node n : list) {
            if(n.tile.equals(vector)) return true;
        }
        return false;
    }

    private double getDistance(Vector2i tile, Vector2i goal){ // For Nodes
        double dx = tile.getX() - goal.getX();
        double dy = tile.getY() - goal.getY();
        return Math.sqrt(dx*dx + dy*dy); // Phitgoras
    }

    // Func to find nearby target entities
    public List<Entity> getEntities (Entity e, int radius){
        List<Entity> result = new ArrayList<Entity>();
        double ex = e.getX(); // Position of searching entity
        double ey = e.getY();

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (entity.equals(e)) continue; // Don't count itself
            double x = entity.getX(); // Position of target entity
            double y = entity.getY();
            double dx = Math.abs(x - ex); // Clac distance, mke it positive
            double dy = Math.abs(y - ey);
            double distance = Math.sqrt((dx * dx) + (dy * dy)); // Pythagoras to calc direct path
            if (distance <= radius) result.add(entity); // Return it if in range
        }
        return result;
    }

    // Func to find nearby target Players
    public List<Mob> getPlayers (Entity e, int radius){ // TODO Optimize this and b4 func (int ex is unnecessary)
        List<Mob> result = new ArrayList<Mob>(); // TODO was Player instead of Mob
        double ex = e.getX();
        double ey = e.getY();
        for (int i = 0; i < players.size(); i++) {
            Mob player = players.get(i); // TODO was Player, not Mob
            double x = player.getX(); // Position of target entity
            double y = player.getY();
            double dx = Math.abs(x - ex); // Clac distance, mke it positive
            double dy = Math.abs(y - ey);
            double distance = Math.sqrt((dx * dx) + (dy * dy)); // Pythagoras to calc direct path
            if (distance <= radius) result.add(player); // Return it if in range
        }
        return result;
    }


    public Tile getTile (int x, int y){ //pixel wise
        if (x < 0 || y < 0 || x >= width || y >= height) return Tile.voidTile;//fill out of bounds
        if (tiles[x + y * width] == Tile.col_spawn_grass) return Tile.spawn_grass;
        if (tiles[x + y * width] == Tile.col_spawn_floor) return Tile.spawn_floor;
        if (tiles[x + y * width] == Tile.col_spawn_floor_boards) return Tile.spawn_floor_boards;
        if (tiles[x + y * width] == Tile.col_spawn_hedge) return Tile.spawn_hedge;
        if (tiles[x + y * width] == Tile.col_spawn_wall1) return Tile.spawn_wall1;
        if (tiles[x + y * width] == Tile.col_spawn_portal) return Tile.spawn_portal;
        //unused
        if (tiles[x + y * width] == Tile.col_spawn_wall2) return Tile.spawn_wall2;
        if (tiles[x + y * width] == Tile.col_spawn_water) return Tile.spawn_water;
        return Tile.voidTile;

    }
}
