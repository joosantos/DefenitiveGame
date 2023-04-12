package Graphics.UI;

import Input.Mouse;
import Utils.Vector2i;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class UIButton extends UIComponent implements UIButtonListener{

    private UIButtonListener buttonListener;
    private UILabel label; // txt in btn

    private Image image;

    private boolean inside = false;
    private boolean pressed = false;
    private boolean ignorePressed = false;
    private boolean ignoreAction = false;

    public UIButton(Vector2i position, Vector2i size) {
        super(position, size);
        Vector2i lp = new Vector2i(position);
        lp.x += 4;
        lp.y += size.y - 3; // size.y is bottom of button
        label = new UILabel(lp, ""); // offset both x and y by 5
        label.setColor(0x444444);
        label.active = false;
        color = new Color(0xAAAAAA);
    }

    public UILabel getLabel() {
        return label;
    }

    public UIButton(Vector2i position, BufferedImage image) {
        super(position, new Vector2i(image.getWidth(), image.getHeight()));
        setImage(image);
        color = new Color(0xAAAAAA);
    }

    // Using this so that panel isn't null on load
    void init(UIPanel panel){ // left as default (not pub or priv) so it isn't accessible outside of package?
        super.init(panel);
        if (label != null) panel.addComponent(label);
    }

    public void ignoreNextPress(){
        ignoreAction = true;
    }

    public void tick() {
        Rectangle rect = new Rectangle(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y);
        boolean mouseLeftBtnDown = Mouse.getButton() == MouseEvent.BUTTON1;
        if (rect.contains(new Point(Mouse.getX(), Mouse.getY()))){
            if (!inside){
                if (mouseLeftBtnDown) ignorePressed = true; // Entered while already pressed
                else ignorePressed = false;
                entered(this); // from interface
            }
            inside = true;

            if (!pressed && !ignorePressed && mouseLeftBtnDown){
                pressed(this);
                pressed = true;
            }else if(Mouse.getButton() == MouseEvent.NOBUTTON){
                if (pressed){
                    released(this);
                    if (!ignoreAction){
                        action(this);
                    }
                    pressed = false;
                }
                ignorePressed = false;
            }

        }
        else{
            if (inside){
                exited(this); // from interface
                pressed = false;
            }
            inside = false;
        }

    }

    public void setImage(Image image){
        this.image = image;
    };

    public void setText (String text){
        if (text == "") label.active = false;
        else label.text = text;
    }

    public void render(Graphics g) {
        int x = position.x + offset.x;
        int y = position.y + offset.y;
        if (image != null){
            g.drawImage(image, x, y, null);
        }else{
            g.setColor(color);
            g.fillRect(x, y, size.x, size.y);

            if (label != null){
                label.render(g);
            }
        }
    }

    @Override // from interface
    public void action(UIButton button) {
        //TODO remove this
        System.out.println("pressed");
    }
}
