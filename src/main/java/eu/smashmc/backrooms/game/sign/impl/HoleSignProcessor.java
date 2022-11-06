package eu.smashmc.backrooms.game.sign.impl;

import eu.smashmc.backrooms.config.model.GameConfig;
import eu.smashmc.backrooms.game.GameConstants;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.sign.SignProcessor;
import in.prismar.library.spigot.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HoleSignProcessor implements SignProcessor {


    @Override
    public boolean process(GameProvider provider, Game game, Location location, String[] lines) {
        if(lines[0].equalsIgnoreCase("spawn: hole")) {
            GameConfig config = provider.getConfigProvider().getEntity().getGame();

            Location center = location.getBlock().getLocation().clone().subtract(0, 1, 0);
            Set<Location> air = new HashSet<>();
            for (int i = 0; i < config.getHoleDepth(); i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        Location place = center.clone().add(j, 0, k);
                        place.getBlock().setType(Material.AIR);
                        air.add(place);
                    }
                }
                for (int j = -2; j < 3; j++) {
                    for (int k = -2; k < 3; k++) {
                        Location place = center.clone().add(j, 0, k);
                        if(air.contains(place)) {
                            continue;
                        }
                        place.getBlock().setType(GameConstants.WALL_BLOCK);
                    }
                }

                center = center.subtract(0, 1, 0);
            }
            for (int j = -2; j < 3; j++) {
                for (int k = -2; k < 3; k++) {
                    Location place = center.clone().add(j, 0, k);
                    place.getBlock().setType(GameConstants.WALL_BLOCK);
                }
            }
            return true;
        }
        return false;
    }
}
