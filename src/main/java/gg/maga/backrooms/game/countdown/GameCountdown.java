package gg.maga.backrooms.game.countdown;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public abstract class GameCountdown implements Countdown {

    private BukkitTask task;
    private Plugin plugin;
    private final int maxCount;

    private final Game game;
    private final GameProvider provider;
    private final GameService service;


    @Setter
    private int currentCount;

    public GameCountdown(Plugin plugin, GameProvider provider, GameService service, Game game, int maxCount) {
        this.plugin = plugin;
        this.game = game;
        this.provider = provider;
        this.service = service;
        this.maxCount = maxCount;
        this.currentCount = maxCount;
    }

    @Override
    public void start() {
        onStart();
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(currentCount <= 0) {
                stop(false);
                onEnd();
                return;
            }
            onCount();
            currentCount--;
        }, 20, 20);
    }

    @Override
    public void stop(boolean force) {
        if(isRunning()) {
            task.cancel();
            task = null;
            if(force) {
                onForceStop();
            }
        }

    }

    @Override
    public boolean isRunning() {
        return task != null;
    }
}
