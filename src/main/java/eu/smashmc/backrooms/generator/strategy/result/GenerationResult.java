package eu.smashmc.backrooms.generator.strategy.result;

import eu.smashmc.backrooms.generator.room.PlacedRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
public class GenerationResult {

    private long time;
    private List<PlacedRoom> rooms;
}
