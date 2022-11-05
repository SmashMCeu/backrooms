package eu.smashmc.backrooms.util.raytrace.hitbox;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RaytraceHitboxHelper {

    /**
     * Fov is broken
     *
     * @param origin
     * @param direction
     * @param range
     * @param fov
     * @return
     */
    public static List<RaytraceHitbox> collectPossibleEntityHitboxes(Location origin, Vector direction, double range, double fov) {
        final World world = origin.getWorld();
        final double rangeSqrt = range * range;
        double radians = Math.toRadians(fov / 2);
        List<RaytraceHitbox> hitboxes = new ArrayList<>();
        for(Player player : world.getPlayers()) {
            Location location = player.getLocation();
            if(location.distanceSquared(origin) <= rangeSqrt) {
                double deltaX = location.getX() - origin.getX();
                double deltaY = location.getY() - origin.getY();
                double deltaZ = location.getZ() - origin.getZ();
                Vector targetDirection = new Vector(deltaX, deltaY, deltaZ).normalize();
                double dot = targetDirection.dot(direction);
                if(Math.acos(dot) < radians) {
                    hitboxes.add(new RaytraceEntityHitbox(player));
                }

            }
        }
        return hitboxes;
    }


    public static List<RaytraceHitbox> collectPossibleBlockHitboxes(Location origin, Vector direction, double range) {
        List<RaytraceHitbox> hitboxes = new ArrayList<>();

        Location end = origin.clone().add(direction.clone().normalize().multiply(range));
        double minX = Math.min(origin.getX(), end.getX());
        double minY = Math.min(origin.getY(), end.getY());
        double minZ = Math.min(origin.getZ(), end.getZ());

        double maxX = Math.max(origin.getX(), end.getX());
        double maxY = Math.max(origin.getY(), end.getY());
        double maxZ = Math.max(origin.getZ(), end.getZ());

        for (double x = minX - 1; x <= maxX + 1; x++) {
            for (double y = minY - 1; y <= maxY + 1; y++) {
                for (double z = minZ - 1; z <= maxZ + 1; z++) {
                    Location location = new Location(origin.getWorld(), x, y, z);
                    Block block = location.getBlock();
                    if(block.getType().isSolid()) {
                        hitboxes.add(new RaytraceBlockHitbox(location.getBlock()));
                    }
                }
            }
        }
        return hitboxes;
    }



}
