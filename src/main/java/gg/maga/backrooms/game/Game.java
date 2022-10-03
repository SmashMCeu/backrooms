package gg.maga.backrooms.game;

import gg.maga.backrooms.game.countdown.Countdown;
import gg.maga.backrooms.game.countdown.impl.LobbyCountdown;
import gg.maga.backrooms.game.participant.GameParticipantRegistry;
import gg.maga.backrooms.room.PlacedRoom;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

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

    private final GameRegistry registry;
    private final GameParticipantRegistry participantRegistry;
    private GameProperties properties;
    private GameState state;
    private List<PlacedRoom> rooms;

    private Countdown countdown;

    public Game(GameRegistry registry, GameProperties properties, List<PlacedRoom> rooms) {
        this.registry = registry;
        this.properties = properties;
        this.rooms = rooms;
        this.state = GameState.LOBBY;
        this.participantRegistry = new GameParticipantRegistry();
        this.countdown = new LobbyCountdown(this);
    }
}
