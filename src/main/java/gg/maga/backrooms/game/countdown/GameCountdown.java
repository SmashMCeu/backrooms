package gg.maga.backrooms.game.countdown;

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

    @Setter
    private int currentCount;

    public GameCountdown(Plugin plugin, int maxCount) {
        this.plugin = plugin;
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
