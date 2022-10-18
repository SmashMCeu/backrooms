package gg.maga.backrooms.game.task;

import gg.maga.backrooms.game.GameConstants;
import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistState;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GameMainTask extends BukkitRunnable {

    private final Game game;
    private final GameService service;

    private final GameProvider provider;

    public GameMainTask(GameProvider provider, GameService service, Game game) {
        this.game = game;
        this.provider = provider;
        this.service = service;
    }

    @Override
    public void run() {
        for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            if(participant instanceof ScientistParticipant scientist) {
                Player player = scientist.getPlayer();
                if(scientist.getState() == ScientistState.DEAD) {
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
                } else if(scientist.getState() == ScientistState.ALIVE) {
                    RayTraceResult result =
                            player.getWorld().rayTraceEntities(player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(2)),
                                    player.getLocation().getDirection(), GameConstants.MAX_SOUND_PLAY_RAYTRACE, 1);
                   if(result != null) {
                       if(result.getHitEntity() != null) {
                           if(result.getHitEntity() instanceof Player entityPlayer) {
                               GameParticipant targetParticipant = game.getParticipantRegistry().getParticipant(entityPlayer.getUniqueId());
                               if(targetParticipant instanceof EntityParticipant entity) {
                                   entity.onScientistSee(provider, service, game, scientist);
                               }
                           }
                       }
                   }

                }
            }
        }
    }
}
