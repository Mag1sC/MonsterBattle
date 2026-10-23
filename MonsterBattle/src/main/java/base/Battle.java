package base;

import entities.Entity;
import entities.Player;

import java.util.concurrent.CompletableFuture;

public class Battle {

    private final Player player;
    private final Entity entity;

    public Battle(Player player, Entity entity) {
        this.player = player;
        this.entity = entity;
    }

    public CompletableFuture<Integer> start() {
        return CompletableFuture.supplyAsync(
                this::startBattle,
                World.getConfigExecutor()
        );
    }

    private int startBattle() {
        //TODO
        return -1;
    }
}
