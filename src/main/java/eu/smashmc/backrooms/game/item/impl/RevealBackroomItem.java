package eu.smashmc.backrooms.game.item.impl;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.util.ParticleUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RevealBackroomItem extends CountdownBackroomItem {

    public RevealBackroomItem() {
        super("Reveal", Material.WITHER_SKELETON_SKULL, 37);
        setDisplayName("§cReveal");
        addLore("§7Reveal one scientist");
    }

    @Override
    public void onCountdownInteract(Player player, GameProvider provider, GameService service, Game game, PlayerInteractEvent event) {
        if (game.getState() == GameState.IN_GAME) {
            int alive = service.getAliveParticipants(game);
            if (alive >= 1) {
                ScientistParticipant scientist = service.findNearestParticipant(player, game);
                if(scientist == null) {
                    return;
                }
                new BukkitRunnable() {
                    int count = 15;
                    @Override
                    public void run() {
                        if(count <= 0 || game.getState() != GameState.IN_GAME) {
                            cancel();
                            return;
                        }
                        count--;
                        ParticleUtil.spawnParticleAlongLine(player, player.getLocation().clone().add(0, 0.2, 0), scientist.getPlayer().getLocation()
                                .clone().add(0, 0.2, 0), Particle.FLAME, 150, 0);
                    }
                }.runTaskTimer(provider.getBackrooms(), 0, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT, 0.6f, 1);
            }
        }
    }


}
