package entities;

import additionals.Backpack;
import additionals.Experience;
import items.HealingPotion;
import items.Item;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public final class Player extends Entity {

    private final int maxHp;
    /**
     * Параметр: ловкость игрового персонажа. Чем выше ловкость, тем выше модификатор ловкости. Не ограничен
     */
    private double dexterity = 0.1;
    private int golds = 10;

    /**
     * Рюкзак игрового персонажа. Может хранить зелья и другие предметы
     * */
    private Backpack backpack = Backpack.getDefault();

    public Player(String name, Runnable levelUpAction) {
        super(20, 100, name, levelUpAction);
        maxHp = getHp();
    }

    /**
     * Урон рассчитывается исходя из трех параметров: уровень здоровья, сила, ловкость.
     * <p>
     * При hp = 1, множитель здоровья = 0.5, урон снижен.
     * <p>
     * Чем выше hp, тем ближе множитель к 1, при этом hp не может быть меньше 1
     * <p>
     * Чем выше ловкость, тем выше множитель ловкости.
     */
    @Override
    public int attack(Entity entity) {
        if (isAlive()) {
            throw new IllegalStateException("entity with flag isAlive, that equals false can not attack");
        }
        if (entity.isAlive()) {
            throw new IllegalStateException("entity with flag isAlive, that equals false can not be attacked");
        }

        int successChance = switch (dexterity) {
            case double v when v < 0.3 -> 70;
            case double v when v < 0.5 -> 75;
            case double v when v < 0.7 -> 80;
            case double v when v < 0.9 -> 85;
            default -> 100;
        };
        boolean isSuccessHit = Random.from(RandomGenerator.getDefault()).nextInt(0, 100) <= successChance;

        double hpFactor = (double) getHp() / (getHp() + 1);
        int damage = (int) (getPower() * hpFactor * (1 + dexterity));

        if (isSuccessHit) {
            return entity.takeDamage(damage);
        } else {
            return entity.takeDamage(damage / 3);
        }
    }

    /**
     * Возвращает -3, если индекс выходит за границы массива
     * <p>
     * Возвращает -2, если по индексу ничего нет
     * <p>
     * Возвращает -1, если по индексу иной предмет (не зелье)
     * <p>
     * Возвращает 0, если эффект зелья нельзя применить в данный момент
     * <p>
     * Возвращает 1, если зелье применено
     * */
    public int drinkPotion(int index) {
        if (index < 0 || index > backpack.getStore().size()) {
            return -3;
        }
        Item item = backpack.getStore().get(index);

        if (item == null) {
            return -2;
        } else if (item instanceof HealingPotion potion) {
            if (getHp() == maxHp) {
                return 0;
            }

            int potentialHp = potion.getHealingSize() + getHp();
            setHp(Math.min(potentialHp, maxHp));

            backpack.remove(index);

            return 1;
        } else {
            return -1;
        }
    }

    public List<Item> getBackpack() {
        return backpack.getStore();
    }

    public boolean storeItem(Item item) {
        return backpack.add(item);
    }

    public int getLevel() {
        return super.getLevel();
    }
}
