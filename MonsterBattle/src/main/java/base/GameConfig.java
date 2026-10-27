package base;

import entities.Monster;
import entities.Player;
import items.HealingPotion;
import items.Item;
import utils.Printer;
import utils.RandomValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameConfig {

    private final ExecutorService executor;
    private final Scanner scanner;

    public static GameConfig getInstance() {
        return new GameConfig(Executors.newSingleThreadExecutor());
    }

    private GameConfig (ExecutorService executor) {
        this.executor = executor;
        scanner = new Scanner(System.in);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Player getPlayerInstance() {
        Printer.staticPrint("Введите ваше имя: ");
        String playerName = scanner.nextLine().trim();

        return new Player(playerName, null);
    }

    public Monster getRandomMonsterInstance() {
        int level = RandomValue.getRandomValue(1, 20);
        int power = RandomValue.getRandomValue(5, 10) + level;
        int hp = RandomValue.getRandomValue(50, 80) + 5 * level;
        int critChance = RandomValue.getRandomValue(1, 5);

        String name = "";
        switch (RandomValue.getRandomValue(0, 1)) {
            case 0 -> name = "Гоблин";
            case 1 -> name = "Скелет";
        }

        return new Monster(level, power, hp, name, critChance);
    }

    public Map<Integer, Item> getTraderStore() {
        return getRandomReaderStore();
    }

    /**
     * Возвращает случайный ассортимент торговца.
     * В данной реализации возвращает один и тот же список товаров
     * */
    private Map<Integer, Item> getRandomReaderStore() {
        Map<Integer, Item> items = new HashMap<>();
        items.put(0, new HealingPotion("Зелье лечения I", 15, 10));
        items.put(1, new HealingPotion("Зелье лечения II", 30, 18));
        items.put(2, new HealingPotion("Зелье лечения III", 45, 25));

        items.forEach((index, item) -> {
            if (item instanceof HealingPotion potion) {
                Printer.staticPrintln("\t[" + index + "] " + potion.getTitle() + " (+" + potion.getHealingSize() + " hp) - " + potion.getCost() + " монет");
            }
        });
        return items;
    }
}
