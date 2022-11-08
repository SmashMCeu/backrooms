package eu.smashmc.backrooms.game.task;

import eu.smashmc.backrooms.config.model.BackroomsConfig;
import eu.smashmc.backrooms.config.model.GameConfig;
import eu.smashmc.backrooms.game.GameConstants;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
import eu.smashmc.backrooms.util.UniqueRandomizer;
import eu.smashmc.backrooms.util.raytrace.ScreenEntityFinder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.List;

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

    private final BackroomsConfig config;

    private String lastAmbientSound = "";
    private long nextAmbientSound;

    public GameMainTask(GameProvider provider, GameService service, Game game) {
        this.game = game;
        this.provider = provider;
        this.config = provider.getConfigProvider().getEntity();
        this.service = service;
    }

    @Override
    public void run() {
        long ambientSound = nextAmbientSound - System.currentTimeMillis();
        for (GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            if (ambientSound <= 0) {
                lastAmbientSound = UniqueRandomizer.getRandom(lastAmbientSound, config.getGame().getAmbientSounds());
                participant.getPlayer().playSound(participant.getPlayer().getLocation(), lastAmbientSound, config.getGame().getAmbientVolume(), 1);
            }
            participant.onUpdate(provider, service, game);
        }
        if (ambientSound <= 0) {
            this.nextAmbientSound = System.currentTimeMillis() + 1000 * provider.getConfigProvider().getEntity().getGame().getAmbientRepeat();
        }
    }


}
