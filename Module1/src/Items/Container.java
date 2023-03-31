package Items;
import java.util.ArrayList;

public class Container {
    private ArrayList<Item> items;

    public Container(int size) {
        if (size == 0) return;
        this.items = new ArrayList<Item>(size);
        init();
    }

    public int getSize() {
        return items.size();
    }

    private void init(){
        for (Item obj: items) {
            obj = null;
        }
    }

    public boolean addItem(Item item){
        for (Item obj: items) {
            if (obj == null){
                obj = item;
                return true;
            }
        }
        return true;
    }
}
