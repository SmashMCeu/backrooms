package gg.maga.backrooms;

import gg.maga.backrooms.config.ConfigProvider;
import gg.maga.backrooms.config.model.GeneratorConfig;
import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.generator.BackroomsGenerator;
import gg.maga.backrooms.generator.strategy.impl.PrototypeBackroomsStrategy;
import gg.maga.backrooms.room.scanner.BackroomsScanner;
import gg.maga.backrooms.room.scanner.strategy.PrototypeScannerStrategy;
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
import org.bukkit.plugin.java.JavaPlugin;

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
        this.metaRegistry.build(this.getClassLoader(), "gg.maga.backrooms");

        this.setup.register();

        initializeGenerator();
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
