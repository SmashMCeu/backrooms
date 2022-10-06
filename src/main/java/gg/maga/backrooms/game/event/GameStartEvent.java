package gg.maga.backrooms.game.event;

import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
@RequiredArgsConstructor
public class GameStartEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Game game;


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
