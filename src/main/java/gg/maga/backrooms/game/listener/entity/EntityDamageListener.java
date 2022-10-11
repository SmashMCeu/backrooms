package gg.maga.backrooms.game.listener.entity;

import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageListener implements Listener {

    @Inject
    private GameMatchmaker matchmaker;

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager) {
            if(matchmaker.isInGame(player) && matchmaker.isInGame(damager)) {
                Game game = matchmaker.getGameByPlayer(player).get();
                GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                GameParticipant damagerParticipant = game.getParticipantRegistry().getParticipant(damager.getUniqueId());
                if(participant instanceof ScientistParticipant && damagerParticipant instanceof EntityParticipant) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(matchmaker.isInGame(player))
                if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setCancelled(true);
                }
        }

    }
}
