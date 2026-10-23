package entities;

import additionals.Experience;

public abstract sealed class Entity permits Player, Monster {

    private final String name;
    private int hp;
    private final int power;
    private boolean isAlive = true;

    /**
     * Обертка над опытом. Корректно накапливает опыт, поддерживая повышение уровней
     * */
    private final Experience experience;

    public Entity(int power, int hp, String name, Runnable levelUpAction) {
        this.power = power;
        this.hp = hp;
        this.name = name;
        experience = new Experience(levelUpAction);
    }

    /**
     * Метод получения урона. Если hp опустится ниже нуля - существо помечается, как мертвое.
     * Возвращает полученное количество урона
     * */
    protected int takeDamage(int amount) {
        int damageHilt;
        if (hp - amount < 0) {
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

    protected int getPower() {
        return power;
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
}
