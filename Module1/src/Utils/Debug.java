package Utils;

import Graphics.Screen;

public class Debug {

    private Debug(){
    }

    public static void drawRect (Screen screen, int x, int y, int width, int height, boolean fixed){
        drawRect(screen, x, y, width, height, 0xff0000, fixed); // Colours is Red
    }

    public static void drawRect (Screen screen, int x, int y, int width, int height, int col, boolean fixed){
        screen.drawRect(x, y, width, height, col, fixed);
    }
}
