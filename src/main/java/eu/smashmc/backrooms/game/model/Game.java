package eu.smashmc.backrooms.game.model;

import eu.smashmc.backrooms.game.participant.GameParticipantRegistry;
import eu.smashmc.backrooms.game.countdown.Countdown;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Game {

    private final String id;
    private final GameParticipantRegistry participantRegistry;
    private GameProperties properties;
    private GameState state;
    private GameMap map;

    private BukkitTask mainTask;
    private Countdown countdown;

    private int solvedTasks;

    public Game(String id, GameProperties properties, GameMap map) {
        this.id = id;
        this.properties = properties;
        this.map = map;
        this.state = GameState.LOBBY;
        this.participantRegistry = new GameParticipantRegistry();
    }

    public boolean isThresholdOpened() {
        return solvedTasks >= properties.getMaxTasks();
    }

}
