package eu.smashmc.backrooms.config.model;

import lombok.Data;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class GameConfig {

    private int defaultBackroomsSize = 15;
    private double spaceBetweenBackrooms = 400;
    private int maxScientists = 4;
    private int maxEntities = 1;
    private int minPlayers = 3;
    private int maxTasks = 4;
    private double chestDefaultChance = 0.10;
    private int holeDepth = 20;
    private double holeTeleportHeight = 15;
    private String mapName = "Level 0";

    private int ambientRepeat = 29;
    private float ambientVolume = 0.5f;
    private String[] ambientSounds = {"custom:ambient"};

    private Location lobby;
    private Location generationStart;
}
