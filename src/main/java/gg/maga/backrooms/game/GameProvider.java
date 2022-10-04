package gg.maga.backrooms.game;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.config.GameConfig;
import gg.maga.backrooms.game.sign.GameSignProcessor;
import gg.maga.backrooms.game.sign.SpawnSignProcessor;
import gg.maga.backrooms.generator.strategy.result.GenerationResult;
import gg.maga.backrooms.room.PlacedRoom;
import in.prismar.library.common.tuple.Tuple;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.location.BlocksProcessor;
import in.prismar.library.spigot.location.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameProvider {

    private final Backrooms backrooms;

    @Inject
    private GameConfig config;

    private Map<String, Game> games;
    private List<GameSignProcessor> signProcessors;

    private Location generationLocation;

    public GameProvider(Backrooms backrooms) {
        this.backrooms = backrooms;
        this.games = new ConcurrentHashMap<>();
        this.signProcessors = new ArrayList<>();
        registerDefaultSignProcessors();
    }

    private void registerDefaultSignProcessors() {
        this.signProcessors.add(new SpawnSignProcessor());
    }

    @SafeInitialize
    private void initialize() {
        this.generationLocation = config.getGenerationLocation();
    }

    public CompletableFuture<Game> prepareGame() {
        CompletableFuture<Game> future = new CompletableFuture<>();
        backrooms.getGenerator().generate(generationLocation, config.getDefaultBackroomsSize()).thenAccept(result -> {
            final String id = UUID.randomUUID().toString();
            Tuple<Location, Location> minMax = getMinMaxOfGeneration(result);
            GameMap map = new GameMap(result, minMax.getFirst(), minMax.getSecond());
            GameProperties properties = new GameProperties(config.getMaxScientists(), config.getMaxEntities());
            Game game = new Game(this, id, properties, map);

            BlocksProcessor processor = new BlocksProcessor(backrooms, minMax.getFirst(), minMax.getSecond(), block -> {
                if(block.getType().name().contains("SIGN")) {
                    Sign sign = (Sign) block.getState();
                    for(GameSignProcessor signProcessor : signProcessors) {
                        boolean remove = signProcessor.process(game, block.getLocation(), sign.getLines());
                        if(remove) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            });
            processor.start().thenAccept(blocks -> {
                register(id, game);
                future.complete(game);
                this.generationLocation = this.generationLocation.subtract(0, 0, config.getSpaceBetweenBackrooms());
            });
        });
        return future;
    }

    public CompletableFuture<Void> clearGames() {
        return CompletableFuture.runAsync(() -> {
            for(Game game : games.values()) {
                try {
                    stopGame(game).get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public CompletableFuture<Game> stopGame(Game game) {
        CompletableFuture<Game> future = new CompletableFuture<>();
        BlocksProcessor processor = new BlocksProcessor(backrooms, game.getMap().getMin(), game.getMap().getMax(), block -> {
            block.setType(Material.AIR);
        });
        processor.start().thenAccept(blocks -> {
            future.complete(game);
            unregister(game.getId());
        });
        return future;
    }

    public Tuple<Location, Location> getMinMaxOfGeneration(GenerationResult generation) {
        PlacedRoom first = generation.getRooms().get(0);
        PlacedRoom last = generation.getRooms().get(generation.getRooms().size() - 1);

        double halfRoomSize = Math.floor(backrooms.getGeneratorConfig().getRoomSize() / 2);
        Location min = first.getCenter().clone().add(-halfRoomSize, 0, halfRoomSize);
        Location max = last.getCenter().clone().add(halfRoomSize, backrooms.getGeneratorConfig().getRoomHeight(), -halfRoomSize);
        return new Tuple<>(min, max);
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
