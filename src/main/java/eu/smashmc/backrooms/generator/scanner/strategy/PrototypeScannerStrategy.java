package eu.smashmc.backrooms.generator.scanner.strategy;

import eu.smashmc.backrooms.generator.room.Room;
import eu.smashmc.backrooms.generator.room.RoomOpening;
import in.prismar.library.common.math.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

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
public class PrototypeScannerStrategy implements ScannerStrategy{

    private double roomSize;
    private double spaceBetweenRooms;
    private double roomHeight;
    private Material baseBlockType;

    private double calcSize;
    private double halfRoomSize;

    public PrototypeScannerStrategy(double roomSize, double spaceBetweenRooms, double roomHeight, Material baseBlockType) {
        this.roomSize = roomSize;
        this.halfRoomSize = Math.floor(roomSize / 2);
        this.roomHeight = roomHeight;
        this.spaceBetweenRooms = spaceBetweenRooms;
        this.baseBlockType = baseBlockType;
        this.calcSize = roomSize + spaceBetweenRooms;
    }


    @Override
    public List<Room> scan(Location start) {
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

            Location up = base.clone().add(0, roomHeight, 0);

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
            if(up.getBlock().getType().name().contains("SIGN")) {
                Sign sign = (Sign) up.getBlock().getState();
                for(String line : sign.getLines()) {
                    if(line.startsWith("name: ")) {
                        room.setName(line.replace("name: ", ""));
                    } else if(line.startsWith("amount: ")) {
                        String[] split = line.replace("amount: ", "").split("-");
                        room.setAmount(MathUtil.random(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
                    } else if(line.startsWith("chance: ")) {
                        room.setChance(Double.valueOf(line.replace("chance: ", "")));
                    }
                }
            }


            return room;
        }
        return null;
    }
}
