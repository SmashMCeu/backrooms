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
import java.util.Random;
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

    private Random random;

    @Setter
    private BackroomsStrategy strategy;

    public BackroomsGenerator(Plugin plugin, Random random) {
        this.plugin = plugin;
        this.random = random;
        this.rooms = new HashMap<>();
    }

    public BackroomsGenerator(Plugin plugin, long seed) {
        this(plugin, new Random(seed));
    }

    public BackroomsGenerator(Plugin plugin) {
        this(plugin, new Random());
    }

    public <T extends GenerationResult> CompletableFuture<T> generate(Location location, int amount) {
        Preconditions.checkNotNull(strategy);
        return this.strategy.generate(location, amount);
    }


}
