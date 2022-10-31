package eu.smashmc.backrooms.game.task;

import eu.smashmc.backrooms.game.GameConstants;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
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
        for (GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            Player player = participant.getPlayer();
            if (participant instanceof ScientistParticipant scientist) {
               if (scientist.getState() == ScientistState.ALIVE) {
                    GameParticipant targetParticipant = raytraceParticipants(player);
                    if (targetParticipant instanceof EntityParticipant entity) {
                        entity.onScientistSee(provider, service, game, scientist);
                    }

                }
            } else if (participant instanceof EntityParticipant entity) {
                GameParticipant targetParticipant = raytraceParticipants(player);
                if (targetParticipant instanceof ScientistParticipant target) {
                    entity.onSeeScientist(provider, service, game, target);
                }
            }
            participant.onUpdate(provider, service, game);
        }
    }

    private GameParticipant raytraceParticipants(Player player) {
        RayTraceResult result =
                player.getWorld().rayTraceEntities(player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(2)),
                        player.getLocation().getDirection(), GameConstants.MAX_SOUND_PLAY_RAYTRACE, 1);
        if (result != null) {
            if (result.getHitEntity() != null) {
                if (result.getHitEntity() instanceof Player entityPlayer) {
                    GameParticipant targetParticipant = game.getParticipantRegistry().getParticipant(entityPlayer.getUniqueId());
                    return targetParticipant;
                }
            }
        }
        return null;
    }
}
