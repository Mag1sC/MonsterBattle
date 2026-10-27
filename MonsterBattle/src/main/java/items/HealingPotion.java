package items;

public final class HealingPotion extends Item {

    private final int healingSize;

    public HealingPotion(String title, int healingSize, int cost) {
        super(title, cost);
        this.healingSize = healingSize;
    }

    public int getHealingSize() {
        return healingSize;
    }
}
