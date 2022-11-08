package eu.smashmc.backrooms.listener;

import eu.smashmc.api.SmashMc;
import eu.smashmc.api.vanish.Vanish;
import eu.smashmc.backrooms.config.ConfigProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.Backrooms;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        if (!player.hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(backrooms, () -> {
                player.teleport(provider.getEntity().getGame().getLobby());
            }, 10L);
        } else {
            player.teleport(provider.getEntity().getGame().getLobby());
        }

        Optional<Game> optional = service.findGame();
        if (optional.isPresent()) {
            Game game = optional.get();
            if (game.getState() == GameState.LOBBY) {
                service.joinGame(optional.get(), player);
            } else if (game.getState() == GameState.IN_GAME) {
                service.joinGameAsSpectator(game, player);
                Vanish<Player> vanish = SmashMc.getComponent(Vanish.class);
                if (vanish.isVanished(player)) {
                    for (Player current : Bukkit.getOnlinePlayers()) {
                        if (current.getUniqueId().equals(player.getUniqueId())) {
                            continue;
                        }
                        current.hidePlayer(backrooms, player);
                    }
                }
            } else {
                player.kickPlayer("§cThe server is shutting down.");
            }

        } else {
            player.kickPlayer("§cPlease wait before the server is fully started.");
        }

    }
}
