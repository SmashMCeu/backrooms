package gg.maga.backrooms.generator;

import gg.maga.backrooms.generator.exception.MissingStrategyException;
import gg.maga.backrooms.generator.strategy.BackroomsStrategy;
import gg.maga.backrooms.generator.strategy.result.GenerationResult;
import gg.maga.backrooms.room.Room;
import jline.internal.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class BackroomsGenerator {

    private Plugin plugin;
    private Map<String, Room> rooms;

    @Setter
    private BackroomsStrategy strategy;

    public BackroomsGenerator(Plugin plugin) {
        this.plugin = plugin;
        this.rooms = new HashMap<>();
    }

    public <T extends GenerationResult> CompletableFuture<T> generate(Location location, int amount) {
        Preconditions.checkNotNull(strategy);
        return this.strategy.generate(location, amount);
    }


}
