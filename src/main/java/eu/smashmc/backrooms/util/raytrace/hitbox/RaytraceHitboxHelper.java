package eu.smashmc.backrooms.util.raytrace.hitbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Maga
 **/
public class RaytraceHitboxHelper {

	public static List<RaytraceHitbox> collectPossibleEntityHitboxes(Location origin, Vector direction, double range, double fov) {
		final World world = origin.getWorld();
		final double rangeSqrt = range * range;
		final var diff = fov - 90;
		final var factor = (double) 4 / 10;
		final var result = diff * factor + 61;
		final var minDotProduct = Math.cos(Math.toRadians(result));
		List<RaytraceHitbox> hitboxes = new ArrayList<>();

		for (Player player : world.getPlayers()) {
			Location location = player.getEyeLocation();
			if (location.distanceSquared(origin) <= rangeSqrt) {
				double deltaX = location.getX() - origin.getX();
				double deltaY = location.getY() - origin.getY();
				double deltaZ = location.getZ() - origin.getZ();
				Vector targetDirection = new Vector(deltaX, deltaY, deltaZ).normalize();
				double dot = targetDirection.dot(direction);

				if (dot > minDotProduct) {
					hitboxes.add(new RaytraceEntityHitbox(player));
				}
			}
		}
		return hitboxes;
	}

	public static List<RaytraceHitbox> collectPossibleBlockHitboxes(Location origin, Vector direction, double range) {
		/* Shared constants */
		final var directionN = direction.clone()
				.normalize();
		final var dirX = directionN.getX();
		final var dirY = directionN.getY();
		final var dirZ = directionN.getZ();
		final var world = origin.getWorld();
		final var x = origin.getX();
		final var y = origin.getY();
		final var z = origin.getZ();

		Set<Block> blocks = new HashSet<>();
		int n = (int) range; // safe to use lower bound as we outline the block
		for (int i = 0; i < n; i++) {
			for (int outlineX = -1; outlineX < 2; outlineX++) {
				for (int outlineY = -1; outlineY < 2; outlineY++) {
					for (int outlineZ = -1; outlineZ < 2; outlineZ++) {
						var position = new Location(world, x + dirX * i + outlineX, y + dirY * i + outlineY, z + dirZ * i + outlineZ);
						var block = position.getBlock();
						if (block.getType()
								.isOccluding()) {
							blocks.add(block);
						}
					}
				}
			}
		}

		List<RaytraceHitbox> hitboxes = new ArrayList<>();
		for (var block : blocks) {
			hitboxes.add(new RaytraceBlockHitbox(block));
		}
		return hitboxes;
	}
}
