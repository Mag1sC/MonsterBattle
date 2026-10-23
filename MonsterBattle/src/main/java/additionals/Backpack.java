package additionals;

import items.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Backpack implements Iterable<Item> {

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

    public Item get(int index) {
        return store.get(index);
    }

    public int size() {
        return store.size();
    }

    @Override
    public Iterator<Item> iterator() {
        return store.iterator();
    }

    @Override
    public void forEach(Consumer<? super Item> action) {
        store.forEach(action);
    }

    @Override
    public Spliterator<Item> spliterator() {
        return store.spliterator();
    }
}
