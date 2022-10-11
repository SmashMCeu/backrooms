package gg.maga.backrooms.game;

import gg.maga.backrooms.config.ConfigProvider;
import gg.maga.backrooms.game.event.GameJoinEvent;
import gg.maga.backrooms.game.event.GameLeaveEvent;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.BacteriaParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.lobby.LobbyParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.scoreboard.CustomBoardRegistry;
import gg.maga.backrooms.game.item.BackroomItemRegistry;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameMatchmaker {

    @Inject
    private GameProvider provider;

    @Inject
    private ConfigProvider configProvider;

    @Inject
    private CustomBoardRegistry boardRegistry;

    @Inject
    private BackroomItemRegistry itemRegistry;

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
        if(!event.isCancelled() && game.getParticipantRegistry().getCount() < game.getProperties().getMaxPlayers()) {
            game.getParticipantRegistry().register(player.getUniqueId(), new LobbyParticipant(player));
            player.teleport(configProvider.getEntity().getGame().getLobby());
            modifyPlayer(player, GameMode.ADVENTURE);

            if(game.getParticipantRegistry().getCount() >= game.getProperties().getMaxPlayers()) {
                if(!game.getCountdown().isRunning()) {
                    game.getCountdown().start();
                }
            }

            boardRegistry.add(player, game);
            boardRegistry.updateTablistAll();

            giveLobbyItems(player);
        }

    }

    public void leaveGame(Game game, Player player, boolean force) {
        if(force) {
            player.teleport(configProvider.getEntity().getGame().getLobby());
            modifyPlayer(player, GameMode.SURVIVAL);
        } else {
            GameLeaveEvent event = new GameLeaveEvent(game, player);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()) {
                player.teleport(configProvider.getEntity().getGame().getLobby());
                modifyPlayer(player, GameMode.SURVIVAL);
                game.getParticipantRegistry().unregister(player.getUniqueId());
                if(game.getParticipantRegistry().getCount() < game.getProperties().getMaxPlayers()) {
                    if(game.getCountdown().isRunning()) {
                        game.getCountdown().stop(true);
                    }
                }
                boardRegistry.remove(player);
                boardRegistry.updateTablistAll();

            }
        }

    }

    public void shuffleParticipants(Game game) {
        GameParticipant[] participants = game.getParticipantRegistry().getParticipants().values().toArray(new GameParticipant[0]);

        int entities = game.getProperties().getMaxEntities();
        for (int i = 0; i < entities; i++) {
            int random = MathUtil.random(game.getParticipantRegistry().getCount() - 1);
            GameParticipant entityParticipant = participants[random];
            game.getParticipantRegistry().register(entityParticipant.getPlayer().getUniqueId(),
                    getRandomEntity(entityParticipant.getPlayer()));
            participants[random] = null;
        }
        for (int i = 0; i < participants.length; i++) {
            GameParticipant participant = participants[i];
            if(participant != null) {
                game.getParticipantRegistry().register(participant.getPlayer().getUniqueId(), new ScientistParticipant(participant.getPlayer()));
            }
        }
    }

    public EntityParticipant getRandomEntity(Player player) {
        return new BacteriaParticipant(player);
    }


    private void modifyPlayer(Player player, GameMode mode) {
        player.setGameMode(mode);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setWalkSpeed(0.2f);
        player.setLevel(0);
        player.setExp(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.getInventory().clear();
    }

    public void giveLobbyItems(Player player) {
        player.getInventory().setItem(8, itemRegistry.createItem("Leave"));
    }

    public void sendMessage(Game game, String message) {
        for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            participant.getPlayer().sendMessage(message);
        }
    }

    public void executeForAll(Game game, Consumer<GameParticipant> consumer) {
        for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            consumer.accept(participant);
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
