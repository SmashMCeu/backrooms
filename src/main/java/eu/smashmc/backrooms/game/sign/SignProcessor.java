package eu.smashmc.backrooms.game.sign;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.model.Game;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface SignProcessor {

    boolean process(GameProvider provider, Game game, Location location, String[] lines);
}
