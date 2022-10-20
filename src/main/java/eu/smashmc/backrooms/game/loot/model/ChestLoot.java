package eu.smashmc.backrooms.game.loot.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface ChestLoot {

    void give(Location chest, Player player);

    double getChance();
}
