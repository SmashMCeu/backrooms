package gg.maga.backrooms.config;

import gg.maga.backrooms.Backrooms;
import in.prismar.library.file.yaml.YamlConfig;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.location.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameConfig extends YamlConfig {

    private Location lobbyLocation;
    private Location generationLocation;

    public GameConfig(Backrooms backrooms) {
        super(backrooms.getDataFolder() + File.separator, "game");
        if(contains("lobbyLocation")) {
            lobbyLocation = LocationUtil.stringToLocation(getGeneric("lobbyLocation"));
        }
        if(contains("generationLocation")) {
            generationLocation = LocationUtil.stringToLocation(getGeneric("generationLocation"));
        }
    }

    public void setGenerationLocation(Location location) {
        set("generationLocation", LocationUtil.locationToString(location));
        this.generationLocation = location;
        save();
    }

    public void setLobbyLocation(Location location) {
        set("lobbyLocation", LocationUtil.locationToString(location));
        this.lobbyLocation = location;
        save();
    }


    public int getDefaultBackroomsSize() {
        return getGeneric("defaultBackroomsSize");
    }

    public double getSpaceBetweenBackrooms() {
        return getGeneric("spaceBetweenBackrooms");
    }

    public int getMaxScientists() {
        return getGeneric("maxScientists");
    }

    public int getMaxEntities() {
        return getGeneric("maxEntities");
    }


}
