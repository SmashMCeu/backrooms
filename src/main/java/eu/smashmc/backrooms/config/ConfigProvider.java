package eu.smashmc.backrooms.config;

import com.google.gson.GsonBuilder;
import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.config.model.BackroomsConfig;
import eu.smashmc.backrooms.config.model.GameConfig;
import eu.smashmc.backrooms.config.model.GeneratorConfig;
import eu.smashmc.backrooms.config.model.participant.ParticipantConfig;
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
            config.setParticipant(ParticipantConfig.create());


            setEntity(config);

            save();
        } else {
            if(getEntity().getParticipant() == null) {
                getEntity().setParticipant(ParticipantConfig.create());
                save();
            }
            if(getEntity().getGame().getAmbientSounds() == null) {
                getEntity().getGame().setAmbientSounds(new String[]{"custom:ambient"});
                save();
            }
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
