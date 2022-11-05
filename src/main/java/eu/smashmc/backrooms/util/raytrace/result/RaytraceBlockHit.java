package eu.smashmc.backrooms.util.raytrace.result;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RaytraceBlockHit extends RaytraceHit<Block> {

    public RaytraceBlockHit(Block target, Location point) {
        super(target, point);
    }
}
