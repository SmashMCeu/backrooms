package gg.maga.backrooms.game;

import gg.maga.backrooms.Backrooms;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameRegistry {

    private final Backrooms backrooms;
    private Map<String, Game> games;

    public GameRegistry(Backrooms backrooms) {
        this.backrooms = backrooms;
        this.games = new HashMap<>();
    }

    public void register(String id, Game game) {
        this.games.put(id, game);
    }

    public void unregister(String id) {
        this.games.remove(id);
    }

    public boolean existsGame(String id) {
        return this.games.containsKey(id);
    }

    public Game getGame(String id) {
        return games.get(id);
    }


}
