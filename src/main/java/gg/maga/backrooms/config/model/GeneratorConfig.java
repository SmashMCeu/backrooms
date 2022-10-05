package gg.maga.backrooms.config.model;

import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class GeneratorConfig {

    private String templateWorld = "backrooms";
    private String floorMaterialType = "HAY_BLOCK";
    private double height = 8;
    private double size = 23;
    private double spaceBetweenRooms = 2;
}
