package base;

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
}
