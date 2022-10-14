package gg.maga.backrooms.game.countdown.impl;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.countdown.GameCountdown;
import gg.maga.backrooms.game.event.GameEndEvent;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class EndCountdown extends GameCountdown {

    private final Game game;

    public EndCountdown(Game game) {
        super(game.getProvider().getBackrooms(), 30);
        this.game = game;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCount() {
        int count = getCurrentCount();
        game.getProvider().getMatchmaker().executeForAll(game, participant -> {
            Player player = participant.getPlayer();
            player.setLevel(count);
        });
    }

    @Override
    public void onForceStop() {

    }

    @Override
    public void onEnd() {
        game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7Thanks for playing §c<3");
        game.getProvider().stopGame(game);
    }
}
