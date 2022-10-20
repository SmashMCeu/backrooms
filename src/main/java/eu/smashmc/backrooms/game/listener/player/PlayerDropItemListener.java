package eu.smashmc.backrooms.game.listener.player;

import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerDropItemListener implements Listener {

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(EntityDropItemEvent event) {
        if(event.getEntity() instanceof Player player) {
            Optional<Game> optional = service.getGameByPlayer(player);
            if(optional.isPresent()) {
                Game game = optional.get();
                GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                if(participant instanceof EntityParticipant) {
                    event.setCancelled(true);
                }

            }
        }
    }
}
