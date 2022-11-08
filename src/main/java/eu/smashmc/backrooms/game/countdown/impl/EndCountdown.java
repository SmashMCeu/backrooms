package eu.smashmc.backrooms.game.countdown.impl;

import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.countdown.GameCountdown;
import eu.smashmc.backrooms.game.model.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class EndCountdown extends GameCountdown {


    public EndCountdown(Plugin plugin, GameProvider provider, GameService service, Game game) {
        super(plugin, provider, service, game, 15);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCount() {
        int count = getCurrentCount();
        getService().executeForAll(getGame(), participant -> {
            Player player = participant.getPlayer();
            player.setLevel(count);
        });
    }

    @Override
    public void onForceStop() {

    }

    @Override
    public void onEnd() {
        getService().sendMessage(getGame(), BackroomsConstants.PREFIX + "§7The game has ended. Thanks for playing §c<3");
        Bukkit.shutdown();
        /*getProvider().stopGame(getGame()).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });*/

    }
}
