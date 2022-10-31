package eu.smashmc.backrooms.game.loot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public abstract class AbstractChestLoot implements ChestLoot {

    private final double chance;
}
