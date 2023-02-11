package Events;

public class Event {

    public enum Type{
        MOUSE_PRESSED,
        MOUSE_RELEASED,
        MOUSE_MOVED
    }

    private Type type;
    boolean handled;

    protected Event(Type type){
        this.type = type;
    }

    public Type getType(){
        return type;
    }
}
