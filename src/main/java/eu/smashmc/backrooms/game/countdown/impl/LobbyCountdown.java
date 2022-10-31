package eu.smashmc.backrooms.game.countdown.impl;

import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.countdown.GameCountdown;
import org.bukkit.plugin.Plugin;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LobbyCountdown extends GameCountdown {


    public LobbyCountdown(Plugin plugin, GameProvider provider, GameService service, Game game) {
        super(plugin, provider, service, game, 60);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCount() {
        int count = getCurrentCount();
        getService().executeForAll(getGame(), participant -> {
            participant.getPlayer().setLevel(count);
        });
        if (count != 0) {
            if (count == 1) {
               getService().sendMessage(getGame(), BackroomsConstants.PREFIX + "§7The game will start in §a" + count + " second");
                return;
            }
            if (count % 20 == 0 || count == 10 || count <= 5) {
               getService().sendMessage(getGame(), BackroomsConstants.PREFIX + "§7The game will start in §a" + count + " seconds");
                return;
            }
        }

    }

    @Override
    public void onForceStop() {
       getService().sendMessage(getGame(), BackroomsConstants.PREFIX + "§7The §ccountdown §7has been forcefully stopped.");
    }

    @Override
    public void onEnd() {
       getService().beginGame(getGame());
    }
}
