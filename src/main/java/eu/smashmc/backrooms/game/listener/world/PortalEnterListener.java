package eu.smashmc.backrooms.game.listener.world;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PortalEnterListener implements Listener {

    @Inject
    private GameProvider provider;

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        if(service.isInGame(player)) {
            event.setCancelled(true);
            Game game = service.getGameByPlayer(player).get();
            GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
            if(participant instanceof ScientistParticipant scientist) {
                service.escape(game, scientist);
            }
        }
    }
}
