package gg.maga.backrooms.room.scanner;

import com.google.common.base.Joiner;
import gg.maga.backrooms.room.Room;
import gg.maga.backrooms.room.RoomOpening;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    private double roomSize;
    private double spaceBetweenRooms;
    private double roomHeight;
    private Location start;
    private Material baseBlockType;

    private double calcSize;
    private double halfRoomSize;

    public BackroomsScanner(Plugin plugin, double roomSize, double spaceBetweenRooms, double roomHeight, Location start, Material baseBlockType) {
        this.plugin = plugin;
        this.roomSize = roomSize;
        this.halfRoomSize = Math.floor(roomSize / 2);
        this.roomHeight = roomHeight;
        this.spaceBetweenRooms = spaceBetweenRooms;
        this.start = start;
        this.baseBlockType = baseBlockType;
        this.calcSize = roomSize + spaceBetweenRooms;
    }

    public List<Room> scan() {
        List<Room> rooms = new ArrayList<>();

        Location location = start.clone();

        Room room = scanSingle(location);

        while (room != null) {
            rooms.add(room);
            location = location.subtract(0, 0, calcSize);
            room = scanSingle(location);

        }

        return rooms;
    }

    private Room scanSingle(Location location) {
        Location base = location.clone().subtract(0, 1, 0);
        if(base.getBlock().getType() == baseBlockType) {
            Location min = base.clone().add(halfRoomSize, 0, halfRoomSize);
            Location max = base.clone().add(-halfRoomSize, roomHeight - 1, -halfRoomSize);

            Set<RoomOpening> openings = new HashSet<>();

            Location north = location.clone().subtract(0, 0, halfRoomSize);
            Location south = location.clone().add(0, 0, halfRoomSize);

            Location east = location.clone().add(halfRoomSize, 0, 0);
            Location west = location.clone().subtract(halfRoomSize, 0, 0);


            if(north.getBlock().getType() == Material.AIR) {
                openings.add(RoomOpening.NORTH);
            }

            if(east.getBlock().getType() == Material.AIR) {
                openings.add(RoomOpening.EAST);
            }

            if(west.getBlock().getType() == Material.AIR) {
                openings.add(RoomOpening.WEST);
            }

            if(south.getBlock().getType() == Material.AIR) {
                openings.add(RoomOpening.SOUTH);
            }
            Room room = new Room(min, max, openings);
            return room;
        }
        return null;
    }

}
