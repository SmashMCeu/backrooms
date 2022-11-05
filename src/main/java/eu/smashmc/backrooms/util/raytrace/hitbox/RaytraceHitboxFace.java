package eu.smashmc.backrooms.util.raytrace.hitbox;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public record RaytraceHitboxFace(Vector a, Vector b, Vector c, Vector d, Vector n) {

    public boolean isBetween(Location location) {
        final double precision = 0.0001;
        double minX = getMinX() - precision;
        double minY = getMinY() - precision;
        double minZ = getMinZ() - precision;
        double maxX = getMaxX() + precision;
        double maxY = getMaxY() + precision;
        double maxZ = getMaxZ() + precision;


        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return x <= maxX && x >= minX && y <= maxY && y >= minY && z <= maxZ && z >= minZ;
    }

    public double getMinX() {
        return min(a.getX(), b.getX(), c.getX(), d.getX());
    }

    public double getMinY() {
        return min(a.getY(), b.getY(), c.getY(), d.getY());
    }

    public double getMinZ() {
        return min(a.getZ(), b.getZ(), c.getZ(), d.getZ());
    }

    public double getMaxX() {
        return max(a.getX(), b.getX(), c.getX(), d.getX());
    }

    public double getMaxY() {
        return max(a.getY(), b.getY(), c.getY(), d.getY());
    }

    public double getMaxZ() {
        return max(a.getZ(), b.getZ(), c.getZ(), d.getZ());
    }


    private double min(double val1, double val2, double val3, double val4) {
        return Math.min(Math.min(val1, val2), Math.min(val3, val4));
    }

    private double max(double val1, double val2, double val3, double val4) {
        return Math.max(Math.max(val1, val2), Math.max(val3, val4));
    }

}
