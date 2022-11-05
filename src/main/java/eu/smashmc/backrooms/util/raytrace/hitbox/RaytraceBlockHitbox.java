package eu.smashmc.backrooms.util.raytrace.hitbox;

import eu.smashmc.backrooms.util.raytrace.result.RaytraceBlockHit;
import eu.smashmc.backrooms.util.raytrace.result.RaytraceHit;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@ToString
public class RaytraceBlockHitbox implements RaytraceHitbox{

    private Block block;

    private Vector minVector;
    private Vector maxVector;

    public RaytraceBlockHitbox(Block block) {
        this.block = block;
        this.minVector = block.getLocation().toVector();
        this.maxVector = block.getLocation().toVector().add(new Vector(1, 1, 1));
    }

    @Override
    public Vector getMaxVector() {
        return maxVector.clone();
    }

    @Override
    public Vector getMinVector() {
        return minVector.clone();
    }

    @Override
    public RaytraceHitboxFace[] getFaces() {
        return facesFromDefaultBox(minVector, maxVector);
    }

    @Override
    public RaytraceHit asHit(Location point) {
        return new RaytraceBlockHit(block, point);
    }
}
