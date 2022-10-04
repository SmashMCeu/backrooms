package gg.maga.backrooms.config;

import gg.maga.backrooms.Backrooms;
import in.prismar.library.file.yaml.YamlConfig;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.meta.processor.impl.ConfigProcessor;
import in.prismar.library.spigot.location.LocationUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class GeneratorConfig extends YamlConfig {

    public GeneratorConfig(Backrooms backrooms) {
        super(backrooms.getDataFolder() + File.separator, "generator");
    }

    public String getRoomTemplateWorld() {
        return getGeneric("roomTemplateWorld");
    }

    public String getRoomFloorMaterialType() {
        return getGeneric("roomFloorMaterialType");
    }

    public double getRoomSize() {
        return getGeneric("roomSize");
    }

    public double getRoomHeight() {
        return getGeneric("roomHeight");
    }

    public double getSpaceBetweenRooms() {
        return getGeneric("spaceBetweenRooms");
    }


}
