package gg.maga.backrooms.game.task;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GameMainTask extends BukkitRunnable {

    private final Game game;
    private final GameService service;

    public GameMainTask(GameService service, Game game) {
        this.game = game;
        this.service = service;
    }

    @Override
    public void run() {
        for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            if(participant instanceof ScientistParticipant scientist) {
                if(scientist.getState() == ScientistState.DEAD) {
                    Player player = scientist.getPlayer();
                    if(player.getSpectatorTarget() == null) {
                        player.setGameMode(GameMode.SPECTATOR);

                        int alive = service.getAliveParticipants(game);
                        if(alive >= 1) {
                            ScientistParticipant random = service.findRandomScientist(game);
                            scientist.setSpectating(random);
                            player.setSpectatorTarget(random.getPlayer());
                        }
                    } else {
                        if(scientist.getSpectating().getState() != ScientistState.ALIVE) {
                            ScientistParticipant random = service.findRandomScientist(game);
                            scientist.setSpectating(random);
                            player.setSpectatorTarget(random.getPlayer());
                        }
                    }
                }
            }
        }
    }
}
