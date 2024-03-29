package Graphics.UI;
import Items.Container;
import Utils.Vector2i;

import java.awt.*;


public class UIInventory{

    private final Container container =  new Container(8);
    private final UIButton[] slots = new UIButton[8];

    public UIInventory (UIPanel panel, Vector2i position, Vector2i size, int offset){
        buildRow(panel, position, size, offset, 4, 0);
        buildRow(panel, position.add(new Vector2i(0, size.y + 3)), size, offset, 8, 4);
    }

    private void buildRow(UIPanel panel, Vector2i position, Vector2i size, int offset, int numBtns, int startingNum){
        for (int i = startingNum; i < numBtns; i++) {
            Vector2i btnPosition = new Vector2i(position.add(new Vector2i((offset + size.x),0)));
            slots[i] = new UIButton(btnPosition, size);
            //slots[i].color = new Color(0xffff00);
            slots[i].setText(String.valueOf(i + 1));
            panel.addComponent(slots[i]);
        }
        // TODO is this the best way? or avoid passing a ref?
        position.subtract(new Vector2i((offset + size.x) * numBtns,0));
    }

}
