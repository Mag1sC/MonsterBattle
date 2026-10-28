package base;

import entities.Player;
import items.Item;
import utils.Command;
import utils.Printer;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class World {

    private final static GameConfig config = GameConfig.getInstance();
    private final Player player;
    private boolean isFinished = false;
    private final Battle battlePlace = new Battle();
    private static Locations currentLocation = Locations.HOME;
    public enum Locations {
        HOME, TRADER, FOREST
    }

    public enum States {
        HOME, NEXT_BATTLE, PLAYER_LOSE, EXIT, ESCAPE
    }

    public static ExecutorService getConfigExecutor() {
        return config.getExecutor();
    }

    public static Scanner getConfigScanner() {
        return config.getScanner();
    }

    public static void main() {
        World world = new World();
        world.start();
    }

    public static Locations getCurrentLocation() {
        return currentLocation;
    }

    private World() {
        Printer.staticPrintln("Добро пожаловать в мир грез и слез!");
        player = config.getPlayerInstance();
    }

    private void start() {
        Printer.onLaunch();
        while (!isFinished) {
            Printer.onEnterCommand();
            Command command = Command.spot(getConfigScanner().nextLine());

            switch (command.getType()) {
                case Command.Commands.HOME -> Printer.staticPrintln("Вы уже находитесь в локации Дом");
                case Command.Commands.TRADER -> toTrader();
                case Command.Commands.FOREST -> toForest();
                case Command.Commands.STAT_ALL, Command.Commands.STAT_PLAYER ->
                        Printer.provide(player).printPlayerStat();
                case Command.Commands.EXIT -> exit();
                default -> Printer.onInvalidCommand();
            }
        }
    }

    /**
     * Предоставляет покупку предметов. В текущей версии реализована только продажа зелья лечения.
     */
    private void toTrader() {
        currentLocation = Locations.TRADER;

        Printer.staticPrintln("Добро пожаловать к торговцу! В данный момент доступны к продаже только зелья лечения");
        Map<Integer, Item> items = config.getTraderStore();

        World.States state = null;
        Printer.staticPrintln("Что будете покупать?");
        while (state == null) {
            Printer.onEnterCommand();

            Command command = Command.spot(getConfigScanner().nextLine());
            switch (command.getType()) {
                case Command.Commands.BUY_ITEM -> {
                    int itemIndex = (int) command.getData();
                    try {
                        Item item = items.get(itemIndex);
                        if (item == null) {
                            throw new IndexOutOfBoundsException();
                        }
                        int result = player.buyItem(item);

                        switch (result) {
                            case -1 -> Printer.staticPrintln("Недостаточно монет!");
                            case 0 -> Printer.staticPrintln("Покупка совершена!");
                            case 1 -> Printer.staticPrintln("Недостаточно места в рюкзаке!");
                            default -> throw new RuntimeException("Необработанный тип результата: " + result);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        Printer.staticPrintln("Неверный номер предмета!");
                    }
                }
                case Command.Commands.SELL_ITEM ->
                        Printer.staticPrintln("В данный момент продажа предметов недоступна");
                case Command.Commands.STAT_ALL, Command.Commands.STAT_PLAYER ->
                        Printer.provide(player).printPlayerStat();
                case Command.Commands.EXIT -> {
                    state = States.EXIT;
                    exit();
                }
                case Command.Commands.HOME -> {
                    state = States.HOME;
                    toHome();
                }
                default -> Printer.onInvalidCommand();
            }
        }
    }

    /**
     * Циклическая обработка битвы. Метод не завершится, пока игрок не покинет Лес.
     */
    private void toForest() {
        currentLocation = Locations.FOREST;
        World.States state = States.NEXT_BATTLE;

        // Обрабатываем результат боя в цикле, чтобы избежать рекурсии
        while (state == States.NEXT_BATTLE) {
            World.States battleState = battlePlace.start(player, config.getRandomMonsterInstance()).join();

            switch (battleState) {
                case HOME -> state = States.HOME;
                case NEXT_BATTLE -> Printer.staticPrintln("Уходим глубже в лес...");
                case PLAYER_LOSE -> state = States.PLAYER_LOSE;
                case ESCAPE -> state = States.ESCAPE;
                case EXIT -> state = States.EXIT;
            }
        }

        switch (state) {
            case HOME, ESCAPE -> toHome();
            default -> exit();
        }
    }

    /**
     * По возвращению в локацию Дом полностью излечивает игрока
     */
    private void toHome() {
        currentLocation = Locations.HOME;
        Printer.staticPrintln("Вы вернулись домой! Что дальше?");
        player.systemHeal();
    }

    private void exit() {
        isFinished = true;
        getConfigExecutor().shutdown();
    }
}
