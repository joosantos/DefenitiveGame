package Graphics;

public class AnimatedSprite extends Sprite{ // Only needs to extends/implement Sprite if want all sprites to be usable here

    private int frame = 0;
    private Sprite sprite;
    private int rate = 8;
    private int time;
    private int length = -1; // of animation (how many diff sprites it has)
    private int[] cycle = new int[] {0,1,0,2};
    private int frameIndex = 0;

    public AnimatedSprite(Spritesheet sheet, int width, int height, int length){
        super(sheet, width, height);
        this.length = length;
        sprite = sheet.getSprites()[0]; // default val
        if (length > sheet.getSprites().length) System.out.println("Error! Length of animation is too long!");
    }


    public void tick(){
        time++;
        if (time % rate == 0) { // Just iterating through this I guess
            if (frame >= length - 1) frame = 0;
            else frame ++;
            frameIndex++; // Smoother walking
            if(frameIndex > 3) frameIndex = 0;
            sprite = sheet.getSprites()[frame];
        }

    }

    public Sprite getSprite(){
        return sprite;
    }

    public void setFrameRate(int frames){
        rate = frames;
    }

    public void setFrame(int index){
        if (index > sheet.getSprites().length - 1){ // To prevent wopsies
            System.out.println("Index out of bounds in " + this);
            return;
        }
        sprite = sheet.getSprites()[index];
    }

}
