package gg.maga.backrooms.game.sign;

import gg.maga.backrooms.game.Game;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpawnSignProcessor implements GameSignProcessor{

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
