package eu.smashmc.backrooms.generator.strategy;

import eu.smashmc.backrooms.generator.strategy.result.GenerationResult;
import org.bukkit.Location;

import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface BackroomsStrategy<T extends GenerationResult> {

    CompletableFuture<T> generate(Location location, int amount);

}
