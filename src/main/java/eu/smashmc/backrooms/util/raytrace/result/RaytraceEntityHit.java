package eu.smashmc.backrooms.util.raytrace.result;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RaytraceEntityHit extends RaytraceHit<Entity> {


    public RaytraceEntityHit(Entity target, Location point) {
        super(target, point);
    }
}
