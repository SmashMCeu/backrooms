package gg.maga.backrooms.game.countdown.impl;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.countdown.GameCountdown;
import gg.maga.backrooms.game.event.GameEndEvent;
import gg.maga.backrooms.game.event.GameStartEvent;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class IngameCountdown extends GameCountdown {


    public IngameCountdown(Plugin plugin, GameProvider provider, GameService service, Game game) {
        super(plugin, provider, service, game, 600);
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
        if (count != 0 && count <= 60) {
            if (count == 1) {
                getService().sendMessage(getGame(), BackroomsConstants.PREFIX + "§7The game will end in §e" + count + " second");
                return;
            }
            if (count % 20 == 0 || count == 10 || count <= 5) {
             getService().sendMessage(getGame(), BackroomsConstants.PREFIX + "§7The game will end in §c" + count + " seconds");
                return;
            }
        }

    }

    @Override
    public void onForceStop() {
    }

    @Override
    public void onEnd() {
        getService().endGame(getGame());
    }
}
