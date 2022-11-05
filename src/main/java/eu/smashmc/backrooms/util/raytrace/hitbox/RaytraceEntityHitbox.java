package eu.smashmc.backrooms.util.raytrace.hitbox;

import eu.smashmc.backrooms.util.raytrace.result.RaytraceEntityHit;
import eu.smashmc.backrooms.util.raytrace.result.RaytraceHit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
@ToString
public class RaytraceEntityHitbox implements RaytraceHitbox {

    private Entity entity;

    private Vector minVector;
    private Vector maxVector;

    public RaytraceEntityHitbox(Entity entity) {
        this.entity = entity;
        this.minVector = entity.getLocation().toVector().subtract(new Vector(0.3f, 0f, 0.3f));
        this.maxVector = entity.getLocation().toVector().add(new Vector(0.3f, 1.8f, 0.3f));
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
        return new RaytraceEntityHit(entity, point);
    }

}
