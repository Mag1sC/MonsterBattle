package utils;

public class Command {

    private final Commands type;
    private final Object data;

    public static Command spot(String textCommand) {
        String txtCommand = textCommand.trim().toLowerCase();

        if (txtCommand.contains("stat")) {
            String[] splitCommand = splitCommand(txtCommand);
            switch (splitCommand.length) {
                case 1 -> {
                    return commandFor(Commands.STAT_ALL);
                }
                case 2 -> {
                    String about = splitCommand[1];
                    if (about.equals("player") || about.equals("me")) {
                        return commandFor(Commands.STAT_PLAYER);
                    } else if (about.equals("entity") || about.equals("monster")) {
                        return commandFor(Commands.STAT_ENTITY);
                    } else {
                        return invalidCommand();
                    }
                }
                default -> {
                    return invalidCommand();
                }
            }
        } else if (txtCommand.isEmpty() || txtCommand.equals("\r\n") || txtCommand.equals("\n") || txtCommand.equals("attack")) {
            return commandFor(Commands.ATTACK);
        } else if (txtCommand.equals("open backpack") || txtCommand.equals("backpack")) {
            return commandFor(Commands.OPEN_BACKPACK);
        } else if (txtCommand.contains("drink")) {
            return complexCommandFor(Commands.DRINK_POTION, txtCommand);
        } else if (txtCommand.contains("buy")) {
            return complexCommandFor(Commands.BUY_ITEM, txtCommand);
        } else if (txtCommand.contains("sell")) {
            return complexCommandFor(Commands.SELL_ITEM, txtCommand);
        } else if (txtCommand.equals("next")) {
            return commandFor(Commands.NEXT);
        } else if (txtCommand.equals("home")) {
            return commandFor(Commands.HOME);
        } else if (txtCommand.equals("exit")) {
            return commandFor(Commands.EXIT);
        } else {
            return invalidCommand();
        }
    }

    private static Command commandFor(Commands type, Object data) {
        return new Command(type, data);
    }

    private static Command commandFor(Commands type) {
        return commandFor(type, null);
    }

    private static Command complexCommandFor(Commands type, String command) {
        String[] splitCommand = splitCommand(command);
        if (splitCommand.length == 2) {
            int itemIndex;
            try {
                itemIndex = Integer.parseInt(splitCommand[1]);
            } catch (NumberFormatException e) {
                return invalidCommand();
            }

            return commandFor(type, itemIndex);
        }

        return invalidCommand();
    }

    private static Command invalidCommand() {
        return commandFor(Commands.INVALID);
    }

    private static String[] splitCommand(String command) {
        return command.split(" ");
    }

    private Command(Commands type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Commands getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public enum Commands {
        STAT_PLAYER, STAT_ENTITY,
        STAT_ALL, ATTACK,
        OPEN_BACKPACK, DRINK_POTION,
        BUY_ITEM, SELL_ITEM,
        NEXT, HOME,
        EXIT, INVALID
    }
}
