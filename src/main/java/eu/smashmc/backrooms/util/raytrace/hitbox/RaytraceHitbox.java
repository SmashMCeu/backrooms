package eu.smashmc.backrooms.util.raytrace.hitbox;

import eu.smashmc.backrooms.util.raytrace.result.RaytraceHit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface RaytraceHitbox {

    static final Vector UP = new Vector(0, 1, 0);
    static final Vector DOWN = new Vector(0, -1, 0);
    static final Vector POSITIVE_Z = new Vector(0, 0, 1);
    static final Vector NEGATIVE_Z = new Vector(0, 0, -1);

    static final Vector POSITIVE_X = new Vector(1, 0, 0);
    static final Vector NEGATIVE_X = new Vector(-1, 0, 0);

    RaytraceHitboxFace[] getFaces();

    RaytraceHit asHit(Location point);

    Vector getMinVector();
    Vector getMaxVector();

    default Vector getCenter() {
        Vector center = getMinVector().add(getMaxVector().subtract(getMinVector()).multiply(0.5));
        return center;
    }

    default RaytraceHitboxFace[] facesFromDefaultBox(Vector min, Vector max) {
        RaytraceHitboxFace top = new RaytraceHitboxFace(
                new Vector(min.getX(), max.getY(), min.getZ()),
                new Vector(min.getX(), max.getY(), max.getZ()),
                new Vector(max.getX(), max.getY(), max.getZ()),
                new Vector(max.getX(), max.getY(), min.getZ()),
                UP
        );
        RaytraceHitboxFace bottom = new RaytraceHitboxFace(
                new Vector(min.getX(), min.getY(), min.getZ()),
                new Vector(min.getX(), min.getY(), max.getZ()),
                new Vector(max.getX(), min.getY(), max.getZ()),
                new Vector(max.getX(), min.getY(), min.getZ()),
                DOWN
        );
        RaytraceHitboxFace front = new RaytraceHitboxFace(
                new Vector(min.getX(), min.getY(), min.getZ()),
                new Vector(min.getX(), max.getY(), min.getZ()),
                new Vector(max.getX(), max.getY(), min.getZ()),
                new Vector(max.getX(), min.getY(), min.getZ()),
                NEGATIVE_Z
        );
        RaytraceHitboxFace back = new RaytraceHitboxFace(
                new Vector(min.getX(), min.getY(), max.getZ()),
                new Vector(min.getX(), max.getY(), max.getZ()),
                new Vector(max.getX(), max.getY(), max.getZ()),
                new Vector(max.getX(), min.getY(), max.getZ()),
                POSITIVE_Z
        );

        RaytraceHitboxFace left = new RaytraceHitboxFace(
                new Vector(min.getX(), min.getY(), min.getZ()),
                new Vector(min.getX(), min.getY(), max.getZ()),
                new Vector(min.getX(), max.getY(), max.getZ()),
                new Vector(min.getX(), max.getY(), min.getZ()),
                NEGATIVE_X
        );

        RaytraceHitboxFace right = new RaytraceHitboxFace(
                new Vector(max.getX(), min.getY(), min.getZ()),
                new Vector(max.getX(), min.getY(), max.getZ()),
                new Vector(max.getX(), max.getY(), max.getZ()),
                new Vector(max.getX(), max.getY(), min.getZ()),
                POSITIVE_X
        );
        return new RaytraceHitboxFace[]{top, bottom, front, back, right, left};
    }

}
