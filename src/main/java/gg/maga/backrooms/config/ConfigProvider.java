package gg.maga.backrooms.config;

import com.google.gson.GsonBuilder;
import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.config.model.BackroomsConfig;
import gg.maga.backrooms.config.model.GameConfig;
import gg.maga.backrooms.config.model.GeneratorConfig;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class ConfigProvider extends GsonFileWrapper<BackroomsConfig> {

    public ConfigProvider(Backrooms backrooms) {
        super(backrooms.getDataFolder() + File.separator + "config.json", BackroomsConfig.class);
        load();
        if(!exists()) {
            BackroomsConfig config = new BackroomsConfig();
            config.setGenerator(new GeneratorConfig());

            GameConfig game = new GameConfig();
            game.setGenerationStart(new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
            game.setLobby(new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
            config.setGame(game);


            setEntity(config);

            save();
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
