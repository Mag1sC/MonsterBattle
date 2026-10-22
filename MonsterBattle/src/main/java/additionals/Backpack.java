package additionals;

import items.Item;

import java.util.ArrayList;
import java.util.List;

public class Backpack {

    private final String title;
    private final List<Item> store;
    private final int limit;

    /**
     * Возвращает дефолтный рюкзак для инициализации по умолчанию.
     * */
    public static Backpack getDefault() {
        return new Backpack("Малый рюкзак", 4);
    }

    public Backpack(String title, int backpackLimit) {
        this.title = title;
        limit = backpackLimit;

        store = new ArrayList<>(limit);
    }

    public List<Item> getStore() {
        return new ArrayList<>(store);
    }

    public String getTitle() {
        return title;
    }

    public boolean add(Item item) {
        if (store.size() < limit) {
            store.add(item);
            return true;
        } else {
            return false;
        }
    }

    public boolean add(List<Item> items) {
        if (store.size() + items.size() < limit) {
            store.addAll(items);
            return true;
        } else {
            return false;
        }
    }

    public void remove(int index) {
        store.remove(index);
    }
}
