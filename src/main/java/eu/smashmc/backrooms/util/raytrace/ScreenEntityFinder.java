package eu.smashmc.backrooms.util.raytrace;

import eu.smashmc.backrooms.util.raytrace.hitbox.RaytraceEntityHitbox;
import eu.smashmc.backrooms.util.raytrace.hitbox.RaytraceHitbox;
import eu.smashmc.backrooms.util.raytrace.hitbox.RaytraceHitboxHelper;
import eu.smashmc.backrooms.util.raytrace.result.RaytraceEntityHit;
import eu.smashmc.backrooms.util.raytrace.result.RaytraceHit;
import eu.smashmc.backrooms.util.raytrace.result.RaytraceResult;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Maga
 **/
@AllArgsConstructor
public class ScreenEntityFinder {

	private Player player;

	public List<Entity> findEntities(double range, int fov) {
		List<Entity> entities = new ArrayList<>();
		Location origin = player.getEyeLocation();
		Vector direction = player.getLocation()
				.getDirection();
		List<RaytraceHitbox> entityHitboxes = RaytraceHitboxHelper.collectPossibleEntityHitboxes(origin, direction, range, fov);

		for (RaytraceHitbox entityBox : entityHitboxes) {
			if (entityBox instanceof RaytraceEntityHitbox entityBoxCast) {
				if (entityBoxCast.getEntity()
						.equals(player)) {
					continue;
				}
			}
			if (!isOcluded(entityBox, range)) {
				entities.add(((RaytraceEntityHitbox) entityBox).getEntity());
			}
		}
		return entities;
	}

	private boolean isOcluded(RaytraceHitbox hitbox, double range) {
		Location eyeLocation = player.getEyeLocation();
		final World world = eyeLocation.getWorld();
		List<Location> points = new ArrayList<>();
		Vector max = hitbox.getMaxVector();
		Vector min = hitbox.getMinVector();
		points.add(min.toLocation(world));
		points.add(max.toLocation(world));
		points.add(new Vector(min.getX(), min.getY(), max.getZ()).toLocation(world));
		points.add(new Vector(min.getX(), max.getY(), min.getZ()).toLocation(world));
		points.add(new Vector(max.getX(), min.getY(), min.getZ()).toLocation(world));
		points.add(new Vector(max.getX(), max.getY(), min.getZ()).toLocation(world));
		points.add(new Vector(min.getX(), max.getY(), max.getZ()).toLocation(world));
		points.add(new Vector(max.getX(), min.getY(), max.getZ()).toLocation(world));

		var center = hitbox.getCenter();
		Vector directionToCenter = center.clone()
				.subtract(eyeLocation.toVector());
		double rangeToCenter = center.distance(eyeLocation.toVector());
		List<RaytraceHitbox> blocks = RaytraceHitboxHelper.collectPossibleBlockHitboxes(eyeLocation, directionToCenter, rangeToCenter);
		blocks.add(hitbox);
		for (Location point : points) {
			Vector directionToPoint = point.toVector()
					.subtract(eyeLocation.toVector());
			Raytrace raytrace = new Raytrace(eyeLocation, directionToPoint, blocks);
			RaytraceResult result = raytrace.ray(range);
			var hits = result.getHits();

			double closestHitSqrt = range * range;
			RaytraceHit closestHit = null;

			for (var hit : hits) {
				var distanceSqrt = hit.getPoint()
						.distanceSquared(eyeLocation);
				if (distanceSqrt < closestHitSqrt) {
					closestHitSqrt = distanceSqrt;
					closestHit = hit;
				}
			}

			// As we shoot an ray into the exact position of the target hitbox, it should
			// always be found in the raytrace
			if (closestHit == null) {
				throw new IllegalStateException("Implementation broken");
			}

			if (closestHit instanceof RaytraceEntityHit) {
				return false;
			}
		}
		return true;
	}

}
