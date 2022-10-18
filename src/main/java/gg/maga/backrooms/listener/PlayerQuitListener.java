package gg.maga.backrooms.listener;

import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerQuitListener implements Listener {

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();

        Optional<Game> optional = service.getGameByPlayer(player);
        if(optional.isPresent()) {
            Game game = optional.get();
            service.leaveGame(game, player, false);
        }
    }
}
