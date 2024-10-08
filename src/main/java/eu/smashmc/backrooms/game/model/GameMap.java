package eu.smashmc.backrooms.game.model;

import eu.smashmc.backrooms.generator.strategy.result.GenerationResult;
import in.prismar.library.common.math.MathUtil;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class GameMap {

    private GenerationResult result;
    private Location min;
    private Location max;
    private List<Location> scientistSpawns;
    private List<Location> entitySpawns;
    private List<Location> chests;
    private List<Block> portalBlocks;

    public GameMap(GenerationResult result, Location min, Location max) {
        this.result = result;
        this.min = min;
        this.max = max;
        this.scientistSpawns = new ArrayList<>();
        this.entitySpawns = new ArrayList<>();
        this.portalBlocks = new ArrayList<>();
        this.chests = new ArrayList<>();
    }

    public Location getRandomScientistSpawn() {
        return scientistSpawns.get(MathUtil.random(scientistSpawns.size() - 1));
    }

    public Location getRandomEntitySpawn() {
        return entitySpawns.get(MathUtil.random(entitySpawns.size() - 1));
    }
}
