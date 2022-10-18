package gg.maga.backrooms.game.item.impl;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RevealBackroomItem extends CountdownBackroomItem {

    public RevealBackroomItem() {
        super("Reveal", Material.WITHER_SKELETON_SKULL, 30);
        setDisplayName("§cReveal");
        addLore("§7Reveal one scientist");
    }

    @Override
    public void onCountdownInteract(Player player, GameProvider provider, GameService service, Game game, PlayerInteractEvent event) {
        if (game.getState() == GameState.IN_GAME) {
            int alive = service.getAliveParticipants(game);
            if (alive >= 1) {
                ScientistParticipant scientist = service.findRandomScientist(game);

                new BukkitRunnable() {
                    int count = 10;
                    @Override
                    public void run() {
                        if(count <= 0 || game.getState() != GameState.IN_GAME) {
                            cancel();
                            return;
                        }
                        count--;
                        ParticleUtil.spawnParticleAlongLine(player, player.getEyeLocation(), scientist.getPlayer().getEyeLocation(), Particle.FLAME, 150, 0);
                    }
                }.runTaskTimer(provider.getBackrooms(), 0, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT, 0.6f, 1);
            }
        }
    }


}
