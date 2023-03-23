package Graphics.UI;

import Level.Level;
import Utils.Vector2i;

import java.awt.image.BufferedImage;

public class UIButtonPortal extends UIButton implements UIButtonListener{
    public short levelReference;


    public UIButtonPortal(Vector2i position, Vector2i size) {
        super(position, size);
    }

    public UIButtonPortal(Vector2i position, BufferedImage image) {
        super(position, image);
    }


}
