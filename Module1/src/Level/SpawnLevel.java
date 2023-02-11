package Level;

import Entity.Mob.Chaser;
import Entity.Mob.Dummy;
import Entity.Mob.Shooter;
import Entity.Mob.Star;
import Level.Tile.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpawnLevel extends Level{ //ep 52

    // private int[] levelPixels;

    public SpawnLevel (String path){
        super(path);
    }

    protected void loadLevel(String path) {
        try {
            //System.out.println("Path is: " + new File(path).getAbsolutePath());
            BufferedImage image = ImageIO.read( new File(path) );

            int w = width = image.getWidth();//setting width
            int h = height = image.getHeight();//setting height
            tiles = new int[w * h];
            image.getRGB(0, 0, w, h, tiles, 0, w);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception! Could not load level file!");
        }
        add(new Shooter(10,55));
        add(new Shooter(20,55));
        add(new Shooter(20,60));

    }

        protected void generateLevel(){ //could have return Tile[], but this runs faster

        }

    }
