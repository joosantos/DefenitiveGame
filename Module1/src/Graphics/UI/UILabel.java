package Graphics.UI;

import java.awt.*;

import Utils.Vector2i;

public class UILabel extends UIComponent {

    public String text;
    private Font font;
    public Boolean dropShadow = false;
    public int dropShadowOffset = 2; // 2 px is default

    public UILabel(Vector2i position, String text) {
        super(position);
        font = new Font("Helvetica", Font.PLAIN, 32);
        this.text = text;
        color = new Color(0xff00ff);
    }

    public UILabel setFont(Font font){
        this.font = font;
        return this;
    }

    public void tick(){

    }

    public void render (Graphics g){
        g.setFont(font); // here for efficiency

        if (dropShadow){
            // Doing  black outline on words
            g.setColor(Color.BLACK);
            //g.setFont(font);
            g.drawString(text, position.x + offset.x + dropShadowOffset, position.y + offset.y + dropShadowOffset);
        }
        g.setColor(color);
        //g.setFont(font);
        g.drawString(text, position.x + offset.x, position.y + offset.y);
    }
}
