package gg.maga.backrooms.game;

import gg.maga.backrooms.game.countdown.Countdown;
import gg.maga.backrooms.game.countdown.impl.LobbyCountdown;
import gg.maga.backrooms.game.participant.GameParticipantRegistry;
import lombok.Getter;
import lombok.Setter;

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

    private Countdown countdown;

    public Game(GameProvider provider, String id, GameProperties properties, GameMap map) {
        this.provider = provider;
        this.id = id;
        this.properties = properties;
        this.map = map;
        this.state = GameState.LOBBY;
        this.participantRegistry = new GameParticipantRegistry();
        this.countdown = new LobbyCountdown(this);
    }
}
