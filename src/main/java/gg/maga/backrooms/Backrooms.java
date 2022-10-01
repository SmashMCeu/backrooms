package gg.maga.backrooms;

import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.meta.SpigotCommandProcessor;
import in.prismar.library.spigot.meta.SpigotListenerProcessor;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.meta.anno.AutoListener;
import in.prismar.library.spigot.setup.SpigotSetup;
import lombok.Getter;
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


    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        initialize();
    }

    private void initialize() {
        this.setup = new SpigotSetup(this, "backrooms");
        this.metaRegistry = new MetaRegistry();
        this.metaRegistry.registerProcessor(AutoCommand.class, new SpigotCommandProcessor(setup, metaRegistry));
        this.metaRegistry.registerProcessor(AutoListener.class, new SpigotListenerProcessor(setup, metaRegistry));
        this.metaRegistry.registerEntity(this);
        this.metaRegistry.scan(this.getClassLoader(), "gg.maga.backrooms");
        this.metaRegistry.load();

        this.setup.register();

    }
}
