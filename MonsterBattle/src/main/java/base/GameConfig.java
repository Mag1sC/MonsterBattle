package base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameConfig {

    private final ExecutorService executor;

    public static GameConfig getInstance() {
        return new GameConfig(Executors.newSingleThreadExecutor());
    }

    private GameConfig (ExecutorService executor) {
        this.executor = executor;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
