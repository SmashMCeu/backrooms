package gg.maga.backrooms.game;

import gg.maga.backrooms.game.event.GameJoinEvent;
import gg.maga.backrooms.game.event.GameLeaveEvent;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.lobby.LobbyParticipant;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class GameMatchmaker {

    @Inject
    private GameProvider provider;

    public Optional<Game> findGame() {
        for(Game game : provider.getGames().values()) {
            if(game.getState() == GameState.LOBBY) {
                if(game.getParticipantRegistry().getCount() < game.getProperties().getMaxPlayers()) {
                    return Optional.of(game);
                }
            }
        }
        return Optional.empty();
    }

    public void joinGame(Game game, Player player) {
        GameJoinEvent event = new GameJoinEvent(game, player);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            game.getParticipantRegistry().register(player.getUniqueId(), new LobbyParticipant(player));
        }

    }

    public void leaveGame(Game game, Player player) {
        GameLeaveEvent event = new GameLeaveEvent(game, player);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            game.getParticipantRegistry().unregister(player.getUniqueId());
        }
    }

    public void sendMessage(Game game, String message) {
        for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            participant.getPlayer().sendMessage(message);
        }
    }

    public Optional<Game> getGameByPlayer(Player player) {
        for(Game game : provider.getGames().values()) {
            if(game.getParticipantRegistry().existsParticipant(player.getUniqueId())) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    public boolean isInGame(Player player) {
        return getGameByPlayer(player).isPresent();
    }
}
