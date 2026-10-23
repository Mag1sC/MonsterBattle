package entities;

import java.util.Random;
import java.util.random.RandomGenerator;

public final class Monster extends Entity {

    /**
     * Параметр: шанс критического урона.
     * Должен быть от 1 включительно до 5 включительно.
     * В ином случае будет выброшено IllegalArgumentException
     * */
    private final int critChance;

    public Monster(int power, int hp, String name, int critChance, Runnable levelUpAction) {
        super(power, hp, name, levelUpAction);

        if (critChance < 1 || critChance > 5) {
            throw new IllegalArgumentException("argument critChance can not be less then 1 and more then 5. Current value = " + critChance);
        }
        this.critChance = critChance;
    }

    /**
     * Определяет следующую механику атаки:
     * с вероятностью 10% удар - неудачный (0 урона)
     * с вероятностью critChance * 10 - критический урон (наносит power * 2)
     * в ином случае - обычный удар (наносит power).
     * <p>
     * Бросает IllegalStateException, если любой Entity "мертв"
     * */
    @Override
    public int attack(Entity entity) {
        if (isAlive()) {
            throw new IllegalStateException("entity with flag isAlive, that equals false can not attack");
        }
        if (entity.isAlive()) {
            throw new IllegalStateException("entity with flag isAlive, that equals false can not be attacked");
        }

        int chance = Random.from(RandomGenerator.getDefault()).nextInt(1, 100);
        if (chance < 10) {
            return 0;
        } else if (chance < (1 + critChance) * 10) {
            int damage = getPower() * 2;
            return entity.takeDamage(damage);
        } else {
            return entity.takeDamage(getPower());
        }
    }

    public int getLevel() {
        return super.getLevel();
    }
}
