package base;

import entities.Player;
import utils.Command;
import utils.Printer;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class World {

    private final static GameConfig config = GameConfig.getInstance();
    private final Player player;
    private boolean isFinished = false;
    private final Battle battlePlace = new Battle();

    public enum States {
        HOME, NEXT_BATTLE, PLAYER_LOSE, EXIT, ESCAPE
    }

    public static ExecutorService getConfigExecutor() {
        return config.getExecutor();
    }

    public static Scanner getConfigScanner() {
        return config.getScanner();
    }

    public static void main(String[] args) {
        World world = new World();
        world.start();
    }

    private World() {
        Printer.staticPrintln("Добро пожаловать в мир грез и слез!");
        player = config.getPlayerInstance();
    }

    private void start() {
        Printer.onLaunch();
        while (!isFinished) {
            Printer.staticPrint("Введите команду: ");
            Command command = Command.spot(getConfigScanner().nextLine());

            switch (command.getType()) {
                case Command.Commands.HOME -> Printer.staticPrintln("Вы уже находитесь в локации Дом");
                case Command.Commands.TRADER -> toTrader();
                case Command.Commands.FOREST -> toForest();
                case Command.Commands.STAT_ALL, Command.Commands.STAT_PLAYER ->
                        Printer.provide(player).printPlayerStat();
                case Command.Commands.EXIT -> exit();
                default -> Printer.staticPrintln("Не удалось выполнить команду");
            }
        }
    }

    private void toTrader() {
        Printer.staticPrintln("Торговец еще не вышел на работу!");
    }

    /**
     * Циклическая обработка битвы. Метод не завершится, пока игрок не покинет Лес.
     */
    private void toForest() {
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
        Printer.staticPrintln("Вы вернулись домой! Что дальше?");
        player.systemHeal();
    }

    private void exit() {
        isFinished = true;
        getConfigExecutor().shutdown();
    }
}
