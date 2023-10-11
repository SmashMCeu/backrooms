package eu.smashmc.backrooms.game.model;

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
    private int minPlayers;

    private int maxTasks;

    public int getMaxPlayers() {
        return maxScientists + maxEntities;
    }

    public void setMaxPlayers(int players) {
        this.maxScientists = players - 1;
        this.maxEntities = 1;
    }
}
