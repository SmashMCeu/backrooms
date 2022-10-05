package gg.maga.backrooms.room.scanner;

import com.google.common.base.Joiner;
import gg.maga.backrooms.room.Room;
import gg.maga.backrooms.room.RoomOpening;
import gg.maga.backrooms.room.scanner.strategy.ScannerStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
