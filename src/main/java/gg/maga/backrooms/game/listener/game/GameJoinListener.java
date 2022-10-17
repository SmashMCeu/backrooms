package gg.maga.backrooms.game.listener.game;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.event.GameJoinEvent;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class GameJoinListener implements Listener {

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(GameJoinEvent event) {
        Player player = event.getPlayer();
        Game game = event.getGame();
        int size = game.getParticipantRegistry().getCount() + 1;

        final String joinMessage = BackroomsConstants.PREFIX + "§a" + player.getName() + " §7joined the game. §8[§a" + size + "§8/§a" +
                game.getProperties().getMaxPlayers() + "§8]";

        player.sendMessage(joinMessage);
        service.sendMessage(game, joinMessage);


    }
}

