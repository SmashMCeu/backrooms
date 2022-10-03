package gg.maga.backrooms.game;

import gg.maga.backrooms.game.participant.GameParticipantRegistry;
import gg.maga.backrooms.room.PlacedRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Game {

    private final GameParticipantRegistry participantRegistry;
    private GameProperties properties;
    private GameState state;
    private List<PlacedRoom> rooms;

    public Game(GameProperties properties, List<PlacedRoom> rooms) {
        this.properties = properties;
        this.rooms = rooms;
        this.state = GameState.WARM_UP;
        this.participantRegistry = new GameParticipantRegistry();
    }
}
