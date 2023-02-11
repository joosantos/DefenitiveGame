package Events.Types;

import Events.Event;

public class MouseReleasedEvent extends MouseButtonEvent{
    public MouseReleasedEvent(int button, int x, int y) {
        super(button, x, y, Type.MOUSE_RELEASED);
    }
}
