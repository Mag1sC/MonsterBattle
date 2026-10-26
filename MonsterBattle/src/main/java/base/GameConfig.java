package base;

import entities.Monster;
import entities.Player;
import utils.Printer;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.random.RandomGenerator;

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
        int level = getRandomValue(1, 20);
        int power = getRandomValue(5, 10) + level;
        int hp = getRandomValue(50, 80) + 5 * level;
        int critChance = getRandomValue(1, 5);

        String name = "";
        switch (getRandomValue(0, 1)) {
            case 0 -> name = "Гоблин";
            case 1 -> name = "Скелет";
        }

        return new Monster(level, power, hp, name, critChance);
    }

    /**
     * Возвращает случайное целочисленное значение [from ; to]
     * */
    private int getRandomValue(int from, int to) {
        return Random.from(RandomGenerator.getDefault()).nextInt(from, to + 1);
    }
}
