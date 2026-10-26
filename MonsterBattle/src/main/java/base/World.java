package base;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class World {

    private final static GameConfig config = GameConfig.getInstance();

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

    }
}
