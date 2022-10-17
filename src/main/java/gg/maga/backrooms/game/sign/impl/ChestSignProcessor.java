package gg.maga.backrooms.game.sign.impl;

import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.sign.SignProcessor;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ChestSignProcessor implements SignProcessor {

    @Override
    public boolean process(Game game, Location location, String[] lines) {
        if(lines[0].equals("spawn: chest")) {
            double chance = Double.valueOf(lines[1].replace("chance: ", ""));
            if(Math.random() <= chance) {
                location.getBlock().setType(Material.CHEST);
                game.getMap().getChests().add(location);
                return false;
            }
            return true;
        }
        return false;
    }
}
