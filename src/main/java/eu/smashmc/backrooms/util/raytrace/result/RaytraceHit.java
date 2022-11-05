package eu.smashmc.backrooms.util.raytrace.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
@ToString
public class RaytraceHit<T> {

    private T target;
    private Location point;
}
