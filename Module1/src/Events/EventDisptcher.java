package Events;

public class EventDisptcher {

    private Event event;

    public EventDisptcher(Event event) {
        this.event = event;
    }

    public void dispatch(Event.Type type, EventHandler handler){
        if (event.handled) return;
        if (event.getType() == type) event.handled = handler.onEvent(event);
    }

}
