package entities;

public abstract sealed class Entity permits Player, Monster {

    private final String name;
    private int hp;
    private final int power;
    private boolean isAlive = true;

    public Entity(int power, int hp, String name) {
        this.power = power;
        this.hp = hp;
        this.name = name;
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

    protected int getHp() {
        return hp;
    }
}
