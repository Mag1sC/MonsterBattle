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

        return new Player(playerName, () -> {
            Printer.staticPrintln("Доступно повышение, выберите номер одного из предложенных");
            Printer.staticPrintln("\t[0] +5% к силе");
            Printer.staticPrintln("\t[1] +10% к максимальному уровню здоровья");
            Printer.staticPrintln("\t[2] +0.05 к ловкости");

            while (true) {
                Printer.staticPrint("Введите номер повышения: ");
                String input = scanner.nextLine().trim();
                try {
                    int inputCode = Integer.parseInt(input);
                    if (inputCode >= 0 && inputCode <= 2) {
                        return inputCode;
                    } else {
                        Printer.onInvalidCommand();
                    }
                } catch (Exception _) {
                    Printer.onInvalidCommand();
                }
            }
        });
    }

    public Monster getRandomMonsterInstance() {
        int level = RandomValue.getRandomValue(1, 20);
        int power = RandomValue.getRandomValue(1, 5) + level;
        int hp = RandomValue.getRandomValue(40, 70) + 5 * level;
        int critChance = RandomValue.getRandomValue(1, 3);

        String name = "";
        switch (RandomValue.getRandomValue(0, 1)) {
            case 0 -> name = "Гоблин";
            case 1 -> name = "Скелет";
        }

        return new Monster(level, power, hp, name, critChance);
    }

    public Map<Integer, Item> getTraderStore() {
        return getRandomTraderStore();
    }

    /**
     * Возвращает случайный ассортимент торговца.
     * В данной реализации возвращает один и тот же список товаров
     * */
    private Map<Integer, Item> getRandomTraderStore() {
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
