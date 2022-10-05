package gg.maga.backrooms.game.listener.game;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.event.GameLeaveEvent;
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
public class GameLeaveListener implements Listener {

    @Inject
    private GameMatchmaker matchmaker;

    @EventHandler
    public void onCall(GameLeaveEvent event) {
        Player player = event.getPlayer();
        Game game = event.getGame();
        int size = game.getParticipantRegistry().getCount() - 1;

        matchmaker.sendMessage(game, BackroomsConstants.PREFIX + "§c" + player.getName() + " §7left the game. §8[§c" + size + "§8/§c" +
                game.getProperties().getMaxPlayers() + "§8]");
    }
}

