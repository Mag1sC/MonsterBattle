package utils;

import additionals.Backpack;
import entities.Entity;
import entities.Player;
import items.Item;

public class Printer {

    private final Player player;
    private final Entity entity;

    private Printer(Player player, Entity monster) {
        this.player = player;
        this.entity = monster;
    }

    public static Printer provide(Player player, Entity entity) {
        return new Printer(player, entity);
    }

    public static Printer provide(Player player) {
        return new Printer(player, null);
    }

    public static void staticPrintln(String txt) {
        System.out.println(txt);
    }

    public static void staticPrint(String txt) {
        System.out.print(txt);
    }

    public void onBattleStart() {
        if (entity == null) return;

        println("Начало боя: " + player.getName() + "[" + player.getLevel() + "] vs " + entity.getName() + "[" + entity.getLevel() + "]");
    }

    public void onPlayerMove() {
        print("Ваш ход (введите команду): ");
    }

    public void printStat() {
        if (entity == null) return;

        printPlayerStat();
        println("=========");
        printEntityStat();
    }

    public void printPlayerStat() {
        println(player.getName() + ":");
        println("\t" + "Сила: " + player.getPower());
        println("\t" + "Ловкость: " + player.getDexterity());
        println("\t" + "Здоровье: " + player.getHp() + "/" + player.getMaxHp());
        println("\t" + "Уровень: " + player.getLevel() + " [" + player.getExperienceBucket()[0] + "/" + player.getExperienceBucket()[1] + "]");
    }

    public void printEntityStat() {
        if (entity == null) return;

        println(entity.getName() + ":");
        println("\t" + "Сила: " + entity.getPower());
        println("\t" + "Здоровье: " + player.getHp() + "/" + player.getMaxHp());
        println("\t" + "Уровень: " + entity.getLevel());
    }

    public void printBackpack() {
        Backpack backpack = player.getBackpack();
        println(backpack.getTitle());
        for (int i = 0; i < backpack.size(); i++) {
            Item item = backpack.get(i);
            println("[" + i + "]" + item.getTitle());
        }
    }

    public void onHealingPotionUsed(int hpBefore) {
        println("Зелье выпито. Текущий уровень здоровья: " + player.getHp() + "(+" + (player.getHp() - hpBefore) + ")");
    }

    public void onAttack(Entity attacker, Entity attacked, int damageDealt) {
        println(attacker.getName() + " атаковал " + attacked.getName() + ". Нанесено урона: " + damageDealt);
    }

    public void onInvalidCommand() {
        println("Не удалось выполнить команду");
    }

    public void onUnsupportedCommand() {
        println("Эта команда не может быть здесь использована");
    }

    public void onPlayerWin(long exp, int golds) {
        if (entity == null) return;

        println(player.getName() + " сразил " + entity.getName());
        println("Получено опыта: " + exp);
        println("Получено золота: " + golds);
    }

    public void onPlayerLose() {
        if (entity == null) return;

        println(player.getName() + " был сражен " + entity.getName());
    }

    public void println(String txt) {
        System.out.println(txt);
    }

    public void print(String txt) {
        System.out.print(txt);
    }
}
