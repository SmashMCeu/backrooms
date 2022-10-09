package gg.maga.backrooms.game.countdown.impl;

import gg.maga.backrooms.BackroomsConstants;
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
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class IngameCountdown extends GameCountdown {

    private final Game game;

    public IngameCountdown(Game game) {
        super(game.getProvider().getBackrooms(), 600);
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

            final String actionBarMessage = "§7Tasks§8: §a" + game.getSolvedTasks() + " §8/ §a" + game.getProperties().getMaxTasks();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBarMessage));

        });
        if (count != 0 && count <= 60) {
            if (count == 1) {
                game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7The game will end in §e" + count + " second");
                return;
            }
            if (count % 20 == 0 || count == 10 || count <= 5) {
                game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7The game will end in §c" + count + " seconds");
                return;
            }
        }

    }

    @Override
    public void onForceStop() {
        game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7The §ccountdown §7has been forcefully stopped.");
    }

    @Override
    public void onEnd() {
        game.getProvider().changeState(game, GameState.END);
        //TODO: Find Winner
        game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7The game has §eended");
        Bukkit.getPluginManager().callEvent(new GameEndEvent(game));
    }
}
