package gg.maga.backrooms.room;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
public enum RoomOpening {

    NORTH("SOUTH"),
    SOUTH("NORTH"),
    EAST("WEST"),

    WEST("EAST");

    private final String counterOpening;
}
