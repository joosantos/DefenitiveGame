package Input;

import Events.EventListener;
import Events.Types.MouseMovedEvent;
import Events.Types.MousePressedEvent;
import Events.Types.MouseReleasedEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Mouse implements MouseListener, MouseMotionListener {
    //Can only have on mouse at a time no matter what, so everything is static

    private static int mouseX = -1;
    private static int mouseY = -1;//not using 0 cause (0,0) is n actual coordinate
    private static int mouseB = MouseEvent.NOBUTTON; // 0 is no button in mouse codes

    private EventListener eventListener;

    public Mouse(EventListener listener){
        eventListener = listener;
    }

    public static int getX(){
        return mouseX;
    }
    public static int getY(){
        return mouseY;
    }
    public static int getButton(){
        return mouseB;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(), false);
        eventListener.onEvent(event);
    }

    public void mouseDragged(MouseEvent e) { //Dragged != Moved, it's when pressing and moving
        mouseX = e.getX();
        mouseY = e.getY();

        MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(), true);
        eventListener.onEvent(event);
    }

    public void mousePressed(MouseEvent e) {
        mouseB = e.getButton();

        MousePressedEvent event = new MousePressedEvent(e.getButton(), e.getX(), e.getY());
        eventListener.onEvent(event);
    }

    public void mouseReleased(MouseEvent e) {
        mouseB = MouseEvent.NOBUTTON;

        MouseReleasedEvent event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
        eventListener.onEvent(event);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
}
