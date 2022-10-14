package gg.maga.backrooms.game.countdown.impl;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.event.GameStartEvent;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.countdown.GameCountdown;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        game.getProvider().getMatchmaker().executeForAll(game, participant -> {
            participant.getPlayer().setLevel(count);
        });
        if (count != 0) {
            if (count == 1) {
                game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7The game will start in §a" + count + " second");
                return;
            }
            if (count % 20 == 0 || count == 10 || count <= 5) {
                game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7The game will start in §a" + count + " seconds");
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
        game.getProvider().changeState(game, GameState.IN_GAME);
        game.setCountdown(new IngameCountdown(game));
        game.getCountdown().start();
        game.getProvider().getMatchmaker().shuffleParticipants(game);
        game.getProvider().getMatchmaker().executeForAll(game, participant -> {
            game.getProvider().getMatchmaker().resetPlayer(participant.getPlayer(), GameMode.ADVENTURE);

            Player player = participant.getPlayer();
            if (participant instanceof EntityParticipant entity) {
                Location location = game.getMap().getRandomEntitySpawn();
                player.teleport(location);
                player.sendTitle("§4Entity", "§c" + entity.getName(), 20, 40, 20);
                player.playSound(player.getLocation(), Sound.BLOCK_NETHERRACK_BREAK, 1, 1);
            } else if (participant instanceof ScientistParticipant) {
                Location location = game.getMap().getRandomScientistSpawn();
                player.teleport(location);
                player.sendTitle("§eScientist", "", 20, 40, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
            for (int i = 0; i < 200; i++) {
                player.sendMessage(" ");
            }
        });
        game.getProvider().getMatchmaker().sendMessage(game, BackroomsConstants.PREFIX + "§7The §agame §7has started");
        Bukkit.getPluginManager().callEvent(new GameStartEvent(game));
    }
}
