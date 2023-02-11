package Graphics.UI;

public interface UIButtonListener {

    // Default methods are implicitly public
    default void entered(UIButton button){
        button.setColor(0xcdcdcd);
    }
    default void exited(UIButton button){
        button.setColor(0xAAAAAA);
    }
    default void pressed(UIButton button){
        button.setColor(0xededed);
    }
    default void released(UIButton button){
        button.setColor(0xcdcdcd);
    }
    public void action(UIButton button);
}
