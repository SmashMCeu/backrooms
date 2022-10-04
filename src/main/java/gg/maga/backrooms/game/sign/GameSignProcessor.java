package gg.maga.backrooms.game.sign;

import gg.maga.backrooms.game.Game;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface GameSignProcessor {

    boolean process(Game game, Location location, String[] lines);
}
