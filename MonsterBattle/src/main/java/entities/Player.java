package entities;

import additionals.Experience;

import java.util.Random;
import java.util.random.RandomGenerator;

public final class Player extends Entity {

    /**
     * Параметр: ловкость игрового персонажа. Чем выше ловкость, тем выше модификатор ловкости. Не ограничен
     */
    private double dexterity = 0.1;
    private final Experience experience;
    private int golds = 10;

    public Player(String name, Runnable levelUpAction) {
        super(20, 100, name);
        experience = new Experience(levelUpAction);
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

        double hpFactor = (double) getHp() / (getHp() + 1);
        int damage = (int) (getPower() * hpFactor * (1 + dexterity));

        if (isSuccessHit) {
            return entity.takeDamage(damage);
        } else {
            return entity.takeDamage(damage / 3);
        }
    }
}
