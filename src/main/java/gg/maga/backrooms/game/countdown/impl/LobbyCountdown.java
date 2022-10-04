package gg.maga.backrooms.game.countdown.impl;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.Game;
import gg.maga.backrooms.game.GameState;
import gg.maga.backrooms.game.countdown.GameCountdown;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LobbyCountdown extends GameCountdown {

    private final Game game;

    public LobbyCountdown(Game game) {
        super(game.getProvider().getBackrooms(), 60);
        this.game = game;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCount() {
        int count = getCurrentCount();
        if(count != 0) {
            if(count == 1) {
                Bukkit.broadcastMessage(BackroomsConstants.PREFIX + "§7The game will start in §a" + count + " second");
                return;
            }
            if(count % 20 == 0 || count == 10 || count <= 5) {
                Bukkit.broadcastMessage(BackroomsConstants.PREFIX + "§7The game will start in §a" + count + " seconds");
                return;
            }
        }

    }

    @Override
    public void onEnd() {
        game.setState(GameState.IN_GAME);
        Bukkit.broadcastMessage(BackroomsConstants.PREFIX + "§7The game will start §anow");
    }
}
