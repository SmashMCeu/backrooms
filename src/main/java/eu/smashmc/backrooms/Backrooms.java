package eu.smashmc.backrooms;

import eu.smashmc.backrooms.config.ConfigProvider;
import eu.smashmc.backrooms.config.model.GeneratorConfig;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.generator.scanner.strategy.PrototypeScannerStrategy;
import eu.smashmc.backrooms.generator.BackroomsGenerator;
import eu.smashmc.backrooms.generator.strategy.impl.PrototypeBackroomsStrategy;
import eu.smashmc.backrooms.generator.scanner.BackroomsScanner;
import eu.smashmc.backrooms.util.GameStateHack;
import eu.smashmc.lib.bukkit.SmashLib;
import eu.smashmc.lib.bukkit.server.GameState;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.command.CommandFactory;
import in.prismar.library.spigot.command.exception.impl.NoPermissionException;
import in.prismar.library.spigot.command.exception.impl.PlayerNotFoundException;
import in.prismar.library.spigot.command.exception.impl.PlayerOfflineException;
import in.prismar.library.spigot.command.exception.impl.WrongNumberFormatException;
import in.prismar.library.spigot.hologram.HologramBootstrap;
import in.prismar.library.spigot.meta.SpigotCommandProcessor;
import in.prismar.library.spigot.meta.SpigotListenerProcessor;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.meta.anno.AutoListener;
import in.prismar.library.spigot.setup.SpigotSetup;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service(autoRegister = false)
public class Backrooms extends JavaPlugin {

    private MetaRegistry metaRegistry;
    private SpigotSetup setup;

    @Inject
    private ConfigProvider configProvider;

    @Inject
    private GameProvider gameProvider;

    private BackroomsGenerator generator;
    private BackroomsScanner scanner;

    private HologramBootstrap hologramBootstrap;

    private SmashLib smashLib;
    private GameState gameState;

    @Override
    public void onEnable() {
        initialize();
    }

    private void initialize() {



        this.hologramBootstrap = new HologramBootstrap(this);
        this.setup = new SpigotSetup(this, "backrooms");
        initializeCommandsExceptions();

        this.metaRegistry = new MetaRegistry();
        this.metaRegistry.registerProcessor(AutoCommand.class, new SpigotCommandProcessor(setup, metaRegistry));
        this.metaRegistry.registerProcessor(AutoListener.class, new SpigotListenerProcessor(setup, metaRegistry));
        this.metaRegistry.registerEntity(this);
        this.metaRegistry.build(this.getClassLoader(), "eu.smashmc.backrooms");

        this.setup.register();
        this.smashLib = new SmashLib.SmashLibBuilder().uniqueTablist(true)
                .gameStartedCallback(() -> {
                    Optional<Game> optional = gameProvider.getService().findGame();
                    if(optional.isPresent()) {
                        Game game = optional.get();
                        return game.getState() == eu.smashmc.backrooms.game.model.GameState.IN_GAME;
                    }
                    return false;
                }).build(this);
        this.gameState = new GameStateHack(this);

        initializeGenerator();
        provideGame();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");


        this.gameState.setToLobby();
        this.gameState.setMapName("Level 0");
    }

    private void provideGame() {
        getGameProvider().prepareGame();
    }


    private void initializeCommandsExceptions() {
        CommandFactory.setDefaultExceptionMapper((sender, exception) -> {
            if (exception instanceof NoPermissionException) {
                sender.sendMessage(BackroomsConstants.NO_PERMISSION_MESSAGE);
            } else if (exception instanceof PlayerOfflineException) {
                sender.sendMessage(BackroomsConstants.PLAYER_NOT_ONLINE_MESSAGE);
            } else if (exception instanceof WrongNumberFormatException) {
                sender.sendMessage(BackroomsConstants.NOT_VALID_NUMBER_MESSAGE);
            } else if (exception instanceof PlayerNotFoundException) {
                sender.sendMessage(BackroomsConstants.PLAYER_NOT_FOUND_MESSAGE);
            }
        });
    }

    private void initializeGenerator() {
        GeneratorConfig config = configProvider.getEntity().getGenerator();
        World world = Bukkit.getWorld(config.getTemplateWorld());
        Location start = new Location(world, 0, 0, 0);
        this.scanner = new BackroomsScanner(this, start);
        this.scanner.setStrategy(new PrototypeScannerStrategy(config.getSize(), config.getSpaceBetweenRooms(), config.getHeight(), Material.valueOf(config.getFloorMaterialType())));
        this.generator = new BackroomsGenerator(this);
        this.generator.setRooms(scanner.scan());
        this.generator.setStrategy(new PrototypeBackroomsStrategy(generator, config.getSize()));
    }

}
