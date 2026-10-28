package entities;

import additionals.Experience;

import java.util.List;
import java.util.concurrent.Callable;

public abstract sealed class Entity permits Player, Monster {

    private final String name;
    private int hp;
    private int maxHp;
    private int power;
    private boolean isAlive = true;

    /**
     * Обертка над опытом. Корректно накапливает опыт, поддерживая повышение уровней
     * */
    private final Experience experience;

    /**
     * Конструктор для создания игрового персонажа (Player instance)
     * */
    public Entity(int power, int hp, String name, Callable<Integer> levelUpAction) {
        this.power = power;
        this.hp = hp;
        maxHp = hp;
        this.name = name;
        experience = new Experience(levelUpAction);
    }

    /**
     * Конструктор для создания монстров (Monster instance)
     * */
    public Entity(int level, int power, int hp, String name) {
        this.power = power;
        this.hp = hp;
        maxHp = hp;
        this.name = name;
        experience = new Experience(level);
    }

    /**
     * Метод получения урона. Если hp опустится ниже нуля - существо помечается, как мертвое.
     * Возвращает полученное количество урона
     * */
    protected int takeDamage(int amount) {
        int damageHilt;
        if (hp - amount <= 0) {
            damageHilt = hp;
            isAlive = false;
        } else {
            damageHilt = amount;
        }
        hp -= damageHilt;

        return damageHilt;
    }

    /**
     * Метод атаки другого существа: возвращает силу совершенной атаки.
     * */
    public abstract int attack(Entity entity);

    public boolean isAlive() {
        return isAlive;
    }

    public int getPower() {
        return power;
    }

    protected void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return experience.getLevel();
    }

    public int getHp() {
        return hp;
    }

    protected void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    protected void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public long[] getExperienceBucket() {
        return new long[] {experience.getExperienceBucket(), experience.getExperienceBucketLimit()};
    }

    protected void gainExperience(long exp, List<Integer> levelUps) throws Exception {
        experience.gainExperience(exp, levelUps);
    }

    public String toString(int critChance) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("Entity(")
                .append("name=")
                .append(name)
                .append(", level=")
                .append(getLevel())
                .append(", power=")
                .append(power)
                .append(", critChance=")
                .append(critChance)
                .append(", hp=")
                .append(getMaxHp())
                .append(")");

        return stringBuilder.toString();
    }
}
