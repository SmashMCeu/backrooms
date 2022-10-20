package eu.smashmc.backrooms.game.listener.entity;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageListener implements Listener {

    @Inject
    private GameService service;

    @Inject
    private GameProvider provider;

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager) {
            if(service.isInGame(player) && service.isInGame(damager)) {
                Game game = service.getGameByPlayer(player).get();
                if(game.getState() != GameState.IN_GAME) {
                    return;
                }
                GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                GameParticipant damagerParticipant = game.getParticipantRegistry().getParticipant(damager.getUniqueId());
                if(participant instanceof ScientistParticipant scientistParticipant && damagerParticipant instanceof EntityParticipant entity) {
                    if(scientistParticipant.getState() == ScientistState.ALIVE) {
                        entity.onAttackTarget(provider, service, game, scientistParticipant, event);
                        return;
                    }

                }
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if (service.isInGame(player)) {
                Game game = service.getGameByPlayer(player).get();
                if(game.getState() != GameState.IN_GAME) {
                    event.setCancelled(true);
                    return;
                }
                if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
