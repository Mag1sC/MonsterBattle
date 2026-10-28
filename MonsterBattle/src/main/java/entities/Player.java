package entities;

import additionals.Backpack;
import items.HealingPotion;
import items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.random.RandomGenerator;

public final class Player extends Entity {

    /**
     * Параметр: ловкость игрового персонажа. Чем выше ловкость, тем выше модификатор ловкости. Не ограничен
     */
    private double dexterity = 0.1;
    private int golds = 10;

    /**
     * Рюкзак игрового персонажа. Может хранить зелья и другие предметы
     * */
    private Backpack backpack = Backpack.getDefault();

    public Player(String name, Callable<Integer> levelUpAction) {
        super(30, 100, name, levelUpAction);
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
        if (!isAlive()) {
            throw new IllegalStateException("entity with flag isAlive, that equals false can not attack");
        }
        if (!entity.isAlive()) {
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

        double hpFactor = (double) getHp() / (getMaxHp() + 1);
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
        if (index < 0 || index > backpack.size() || backpack.size() == 0) {
            return -3;
        }
        Item item = backpack.get(index);

        if (item == null) {
            return -2;
        } else if (item instanceof HealingPotion potion) {
            if (getHp() == getMaxHp()) {
                return 0;
            }

            int potentialHp = potion.getHealingSize() + getHp();
            setHp(Math.min(potentialHp, getMaxHp()));

            backpack.remove(index);

            return 1;
        } else {
            return -1;
        }
    }

    public void gainGolds(int amount) {
        if (Integer.MAX_VALUE - amount < golds) return;

        golds += amount;
    }

    public void systemHeal() {
        if (getHp() < getMaxHp()) {
            setHp(getMaxHp());
        }
    }

    /**
     * Метод для покупки предметов. Возвращает: -1, если недостаточно монет, 0, если покупка совершена и сохранена в рюкзак,
     * 1, если недостаточно места в рюкзаке.
     * */
    public int buyItem(Item item) {
        if (golds < item.getCost()) return -1;
        if (storeItem(item)) {
            golds -= item.getCost();
            return 0;
        } else {
            return 1;
        }
    }

    public void gainExperience(long exp) {
        List<Integer> levelUpsAccumulator = new ArrayList<>();
        try {
            super.gainExperience(exp, levelUpsAccumulator);
        } catch (Exception _) {}

        levelUpsAccumulator.forEach(this::getRaise);
    }

    private void getRaise(int code) {
        switch (code) {
            case 0 -> super.setPower((int) (getPower() * 1.05));
            case 1 -> super.setMaxHp((int) (getMaxHp() * 1.1));
            case 2 -> dexterity += 0.05;
            default -> {
                return;
            }
        }
    }

    public Backpack getBackpack() {
        return backpack;
    }

    private boolean storeItem(Item item) {
        return backpack.add(item);
    }

    public int getPower() {
        return super.getPower();
    }

    public double getDexterity() {
        return dexterity;
    }

    public int getGolds() {
        return golds;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("Player(")
                .append("name=")
                .append(getName())
                .append(", level=")
                .append(getLevel())
                .append(", power=")
                .append(getPower())
                .append(", dexterity=")
                .append(dexterity)
                .append(", hp=")
                .append(getMaxHp())
                .append(", golds=")
                .append(golds)
                .append(")");

        return stringBuilder.toString();
    }
}
