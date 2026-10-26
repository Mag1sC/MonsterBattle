package base;

import entities.Entity;
import entities.Player;
import utils.Command;
import utils.Printer;
import utils.RandomValue;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Battle {

    private final Player player;
    private final Entity entity;

    private final Printer printer;
    private final Scanner scanner;

    private boolean isPlayerMove = true;
    private boolean isFirstMove = true;
    private boolean isEscaped = false;
    private int escapeTriesLimit = 1;

    public Battle(Player player, Entity entity) {
        this.player = player;
        this.entity = entity;
        printer = Printer.provide(player, entity);
        scanner = World.getConfigScanner();
    }

    /**
     * Запускает бой асинхронно, возвращает код маршрута
     * */
    public CompletableFuture<World.States> start() {
        return CompletableFuture.supplyAsync(
                this::startBattle,
                World.getConfigExecutor()
        );
    }

    private World.States startBattle() {
        World.States state = null;
        printer.onBattleStart();

        while (player.isAlive() && entity.isAlive() && !(Thread.currentThread().isInterrupted()) && !isEscaped) {
            if (isPlayerMove) {
                providePlayerMove();
            } else {
                provideEntityMove();
            }
        }

        if (isEscaped) {
            return World.States.ESCAPE;
        }

        if (Thread.currentThread().isInterrupted()) {
            return World.States.EXIT;
        }

        if (player.isAlive()) {
            long exp = calculateExperience();
            int golds = calculateGolds();
            player.gainExperience(exp);
            player.gainGolds(golds);
            printer.onPlayerWin(exp, golds);

            printer.println("Текущее состояние:");
            printer.printPlayerStat();
            while (state == null) {
                printer.print("Продолжим убивать нечисть или вернемся домой? (next/home): ");
                Command command = Command.spot(scanner.nextLine());

                switch (command.getType()) {
                    case Command.Commands.NEXT -> state = World.States.NEXT_BATTLE;
                    case Command.Commands.HOME -> state = World.States.HOME;
                    case Command.Commands.EXIT -> state = World.States.EXIT;
                    default -> printer.onInvalidCommand();
                }
            }
        } else {
            state = World.States.PLAYER_LOSE;
            printer.onPlayerLose();
        }

        return state;
    }

    private void providePlayerMove() {
        printer.onPlayerMove();
        String textCommand = scanner.nextLine();
        Command command = Command.spot(textCommand);

        switch (command.getType()) {
            case Command.Commands.ATTACK -> {
                int damageDealt = player.attack(entity);
                printer.onAttack(player, entity, damageDealt);

                isPlayerMove = false;
                if (isFirstMove) {
                    isFirstMove = false;
                }
            }
            case Command.Commands.STAT_ALL -> printer.printStat();
            case Command.Commands.STAT_PLAYER -> printer.printPlayerStat();
            case Command.Commands.STAT_ENTITY -> printer.printEntityStat();
            case Command.Commands.OPEN_BACKPACK -> printer.printBackpack();
            case Command.Commands.DRINK_POTION -> {
                int hpBefore = player.getHp();
                int index = (Integer) command.getData();
                int status = player.drinkPotion(index);

                switch (status) {
                    case -3, -2 -> printer.println("Зелья на такой позиции нет!");
                    case -1 -> printer.println("На такой позиции лежит не зелье. " + player.getBackpack().get(index).getTitle() + " выпить нельзя!");
                    case 0 -> printer.println("Это зелье сейчас не может быть использовано");
                    case 1 -> printer.onHealingPotionUsed(hpBefore);
                }
            }
            case Command.Commands.ESCAPE -> onEscape();
            case Command.Commands.EXIT -> Thread.currentThread().interrupt();
            case Command.Commands.INVALID -> printer.onInvalidCommand();
            default -> printer.onUnsupportedCommand();
        }
    }

    private void provideEntityMove() {
        int damageDealt = entity.attack(player);
        printer.onAttack(entity, player, damageDealt);

        isPlayerMove = true;
    }

    private long calculateExperience() {
        return (long) entity.getPower() * entity.getLevel() / 4;
    }

    private int calculateGolds() {
        return entity.getPower() * entity.getLevel() / 8;
    }

    /**
     * На первом ходу вероятность побега 66%. В случае успешного побега игрок не получает награды.
     * <p>
     * На любом другом ходу вероятность побега 50%. В случае успешного побега игрок получает уменьшенную награду.
     * */
    private void onEscape() {
        if (escapeTriesLimit <= 0) {
            printer.println("Побег невозможен! Попытки исчерпаны");
            return;
        }

        if (isFirstMove) {
            switch (RandomValue.getRandomValue(0, 2)) {
                case 0, 1 -> onSuccessEscape(false);
                case 2 -> onFailedEscape();
            }
        } else {
            switch (RandomValue.getRandomValue(0, 1)) {
                case 0 -> onSuccessEscape(true);
                case 1 -> onFailedEscape();
            }
        }
    }

    private void onFailedEscape() {
        printer.println("Побег не удался!");
        escapeTriesLimit--;
    }

    private void onSuccessEscape(boolean withReward) {
        isEscaped = true;
        printer.println("Побег удался!");
        if (withReward) {
            long exp = calculateExperience() / 3;
            int golds = calculateGolds() / 3;

            printer.onPlayerEscape(exp, golds);
            player.gainExperience(exp);
            player.gainGolds(golds);

            printer.println("Состояние после побега:");
            printer.printPlayerStat();
        }
    }
}
