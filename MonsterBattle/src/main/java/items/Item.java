package items;

public abstract sealed class Item permits HealingPotion{

    private final String title;
    private final int cost;

    public Item(String title, int cost) {
        this.title = title;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }
}
