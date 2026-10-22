package items;

public final class HealingPotion extends Item {

    private final int healingSize;

    public HealingPotion(String title, int healingSize) {
        super(title);
        this.healingSize = healingSize;
    }

    public int getHealingSize() {
        return healingSize;
    }
}
