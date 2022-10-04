package gg.maga.backrooms.config;

import gg.maga.backrooms.Backrooms;
import in.prismar.library.file.yaml.YamlConfig;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.location.LocationUtil;
import org.bukkit.Location;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class GameConfig extends YamlConfig {

    public GameConfig(Backrooms backrooms) {
        super(backrooms.getDataFolder() + File.separator, "game");
    }

    public void setGenerationLocation(Location location) {
        set("generationLocation", LocationUtil.locationToString(location));
    }

    public Location getGenerationLocation() {
        String location = getGeneric("generationLocation");
        return LocationUtil.stringToLocation(location);
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
