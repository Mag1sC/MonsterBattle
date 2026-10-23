package base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class World {

    private final static GameConfig config = GameConfig.getInstance();

    public static ExecutorService getConfigExecutor() {
        return config.getExecutor();
    }

    public static void main(String[] args) {

    }
}
