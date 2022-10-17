package gg.maga.backrooms.generator.scanner;

import gg.maga.backrooms.generator.room.Room;
import gg.maga.backrooms.generator.scanner.strategy.ScannerStrategy;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class BackroomsScanner {

    private Plugin plugin;
    private Location start;

    @Setter
    private ScannerStrategy strategy;

    public BackroomsScanner(Plugin plugin, Location start) {
        this.plugin = plugin;
        this.start = start;
    }

    public List<Room> scan() {
        return strategy.scan(start);
    }



}
