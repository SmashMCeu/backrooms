package eu.smashmc.backrooms.game.item.impl;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.item.event.BackroomItemEvent;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.util.ParticleUtil;
import eu.smashmc.backrooms.util.meta.Register;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Register
public class FlashlightBackroomItem extends BackroomItem {

    private static final double RAY_DISTANCE = 6;

    public FlashlightBackroomItem() {
        super("Flashlight", Material.STICK);
        setDisplayName("§bFlashlight");
        addLore("§7Stun an entity");
    }

    @BackroomItemEvent
    public void onInteract(Player player, GameProvider provider, GameService service,
                           Game game, PlayerInteractEvent event) {
        RayTraceResult result =
                player.getWorld().rayTraceEntities(player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(2)),
                        player.getLocation().getDirection(), RAY_DISTANCE, 1);
        if(result != null) {
            if (result.getHitEntity() != null) {
                if(result.getHitEntity() instanceof Player entityPlayer) {
                    GameParticipant targetParticipant = game.getParticipantRegistry().getParticipant(entityPlayer.getUniqueId());
                    if(targetParticipant instanceof EntityParticipant entity) {
                        entity.stun(provider, service, game, true);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.8f, 1);
                        if (event.getHand() == EquipmentSlot.HAND) {
                            player.getInventory().setItemInMainHand(null);
                        } else if(event.getHand() == EquipmentSlot.OFF_HAND) {
                            player.getInventory().setItemInOffHand(null);
                        }
                        player.updateInventory();
                        ParticleUtil.spawnParticleAlongLine(player, player.getEyeLocation(), entity.getPlayer().getEyeLocation(), Particle.FIREWORKS_SPARK, 10, 0);

                    }
                }
            }
        }

    }
}
