package gg.maga.backrooms.listener;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.config.ConfigProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerJoinListener implements Listener {

    @Inject
    private Backrooms backrooms;

    @Inject
    private ConfigProvider provider;

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        if(!player.hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(backrooms, () -> {
                player.teleport(provider.getEntity().getGame().getLobby());
            }, 10L);
        } else {
            player.teleport(provider.getEntity().getGame().getLobby());
        }

        Optional<Game> optional = service.findGame();
        if(optional.isPresent()) {
            Game game = optional.get();
            if(game.getState() == GameState.LOBBY) {
                service.joinGame(optional.get(), player);
            } else if(game.getState() == GameState.IN_GAME) {
                service.joinGameAsSpectator(game, player);
            } else {
                player.kickPlayer("§cThe server is shutting down.");
            }

        } else {
            player.kickPlayer("§cPlease wait before the server is fully started.");
        }

    }
}
