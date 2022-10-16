package gg.maga.backrooms.game.model;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.countdown.Countdown;
import gg.maga.backrooms.game.countdown.impl.LobbyCountdown;
import gg.maga.backrooms.game.participant.GameParticipantRegistry;
import gg.maga.backrooms.game.task.GameMainTask;
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
    private final GameProvider provider;
    private final GameParticipantRegistry participantRegistry;
    private GameProperties properties;
    private GameState state;
    private GameMap map;

    private BukkitTask mainTask;
    private Countdown countdown;

    private int solvedTasks;

    public Game(GameProvider provider, String id, GameProperties properties, GameMap map) {
        this.provider = provider;
        this.id = id;
        this.properties = properties;
        this.map = map;
        this.state = GameState.LOBBY;
        this.participantRegistry = new GameParticipantRegistry();
        this.countdown = new LobbyCountdown(this);
    }


    public boolean isThresholdOpened() {
        return solvedTasks >= properties.getMaxTasks();
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
