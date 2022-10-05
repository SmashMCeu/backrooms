package gg.maga.backrooms.game.model;

import gg.maga.backrooms.generator.strategy.result.GenerationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

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

    public GameMap(GenerationResult result, Location min, Location max) {
        this.result = result;
        this.min = min;
        this.max = max;
        this.scientistSpawns = new ArrayList<>();
        this.entitySpawns = new ArrayList<>();
    }
}
