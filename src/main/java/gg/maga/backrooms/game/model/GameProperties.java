package gg.maga.backrooms.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameProperties {

    private int maxScientists;
    private int maxEntities;

    public int getMaxPlayers() {
        return maxScientists + maxEntities;
    }
}
