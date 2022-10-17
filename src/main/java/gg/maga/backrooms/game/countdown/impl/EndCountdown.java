package gg.maga.backrooms.game.countdown.impl;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.countdown.GameCountdown;
import gg.maga.backrooms.game.event.GameEndEvent;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        super(plugin, provider, service, game, 30);
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
        getService().sendMessage(getGame(), BackroomsConstants.PREFIX + "§7Thanks for playing §c<3");
        getProvider().stopGame(getGame());
    }
}
