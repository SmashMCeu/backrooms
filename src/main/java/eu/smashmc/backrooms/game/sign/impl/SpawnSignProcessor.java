package eu.smashmc.backrooms.game.sign.impl;

import eu.smashmc.backrooms.game.sign.SignProcessor;
import eu.smashmc.backrooms.game.model.Game;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpawnSignProcessor implements SignProcessor {

    @Override
    public boolean process(Game game, Location location, String[] lines) {
        for(String line : lines) {
            final String lowered = line.toLowerCase();
            if(lowered.startsWith("spawn: scientist")) {
                game.getMap().getScientistSpawns().add(location);
                return true;
            } else if(lowered.startsWith("spawn: entity")) {
                game.getMap().getEntitySpawns().add(location);
                return true;
            }
        }
        return false;
    }
}
