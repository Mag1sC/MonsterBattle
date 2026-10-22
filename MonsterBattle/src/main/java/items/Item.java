package items;

public abstract sealed class Item permits HealingPotion{

    private final String title;

    public Item(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
