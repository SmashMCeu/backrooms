package gg.maga.backrooms.prototype;

import gg.maga.backrooms.room.PlacedRoom;
import gg.maga.backrooms.room.Room;
import gg.maga.backrooms.room.RoomOpening;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.location.copier.Copier;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Prototype {

    private Plugin plugin;
    private Location startLocation;

    private int roomsAmount;
    private double roomSize;
    private List<Room> rooms;
    private List<PlacedRoom> placedRooms;



    public Prototype(Plugin plugin, Location location, double roomSize, int roomsAmount) {
        this.plugin = plugin;
        this.startLocation = location;
        this.roomSize = roomSize;
        this.rooms = new ArrayList<>();
        this.placedRooms = new ArrayList<>();
        this.roomsAmount = roomsAmount;

        scan();
    }

    private void scan() {
        World world = startLocation.getWorld();
        this.rooms.add(new Room(new Location(world, 21, -61, 8),
                new Location(world, -1, -53, -14), RoomOpening.NORTH, RoomOpening.EAST));
        this.rooms.add(new Room(new Location(world, 21, -61, -17),
                new Location(world, -1, -53, -39), RoomOpening.SOUTH, RoomOpening.EAST));
        this.rooms.add(new Room( new Location(world, 21, -61, -42),
                new Location(world, -1, -53, -64), RoomOpening.SOUTH, RoomOpening.EAST));

        this.rooms.add(new Room( new Location(world, 21, -61, -67),
                new Location(world, -1, -53, -89),
                RoomOpening.SOUTH, RoomOpening.EAST, RoomOpening.NORTH, RoomOpening.WEST));

        this.rooms.add(new Room(new Location(world, 21, -61, -92),
                new Location(world, -1, -53, -114),
                RoomOpening.SOUTH, RoomOpening.EAST, RoomOpening.WEST));

        this.rooms.add(new Room(new Location(world, 21, -61, -117),
                new Location(world, -1, -53, -139),
                RoomOpening.SOUTH, RoomOpening.WEST));

        this.rooms.add(new Room(new Location(world, 21, -61, -142),
                new Location(world, -1, -53, -164),
                RoomOpening.SOUTH, RoomOpening.NORTH, RoomOpening.WEST));

        this.rooms.add(new Room(new Location(world, 21, -61, -167),
                new Location(world, -1, -53, -189),
                RoomOpening.NORTH, RoomOpening.WEST));

        this.rooms.add(new Room( new Location(world, 21, -61, -192),
                new Location(world, -1, -53, -214),
                RoomOpening.NORTH, RoomOpening.EAST, RoomOpening.WEST));


        this.rooms.add(new Room(new Location(world, 21, -61, -217),
                new Location(world, -1, -53, -239),
                RoomOpening.NORTH, RoomOpening.EAST, RoomOpening.SOUTH));
    }

    public void start() {
        Room startRoom = null;
        Location startLocation = this.startLocation.clone();

        for (int i = 0; i < roomsAmount; i++) {
            RoomOpening[] counterOpenings = i == 0 ? new RoomOpening[]{} :
                    startRoom.getOpenings().toArray(new RoomOpening[0]);
            for (int j = 0; j < counterOpenings.length; j++) {
                counterOpenings[j] = RoomOpening.valueOf(counterOpenings[j].getCounterOpening());
            }

            Room room;

            if(i == 0) {
                room = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.NORTH, RoomOpening.EAST},
                        new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.WEST});
            } else if(i == roomsAmount - 1) {
                room = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.EAST},
                        new RoomOpening[]{RoomOpening.WEST, RoomOpening.NORTH});
            } else {
                room = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH, RoomOpening.SOUTH},
                        new RoomOpening[]{RoomOpening.WEST});
            }

            Copier copier = new Copier(room.getPivot(), room.getMin(), room.getMax());
            copier.paste(startLocation);

            startRoom = room;


            Location startLocationX = startLocation.clone();
            Room startRoomX = getRandomRoomByOpenings(counterOpenings);

            for (int j = 0; j < roomsAmount; j++) {
                startLocationX = startLocationX.add(roomSize, 0, 0);

                RoomOpening[] counterOpeningsX = startRoomX.getOpenings().toArray(new RoomOpening[0]);
                for (int k = 0; k < counterOpeningsX.length; k++) {
                    counterOpeningsX[k] = RoomOpening.valueOf(counterOpeningsX[k].getCounterOpening());
                }

                Room roomX;
                if(i == 0) {
                    if (j == roomsAmount - 1) {
                        roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.WEST, RoomOpening.NORTH},
                                new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.EAST});
                    } else {
                        roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH, RoomOpening.WEST},
                                new RoomOpening[]{RoomOpening.SOUTH});
                    }
                } else {
                    if(j == roomsAmount - 1) {
                        if(i == roomsAmount - 1) {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.WEST, RoomOpening.SOUTH},
                                    new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH});
                        } else {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.NORTH, RoomOpening.WEST},
                                    new RoomOpening[]{RoomOpening.EAST});
                        }

                    } else {
                        if(i == roomsAmount - 1) {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.WEST, RoomOpening.EAST},
                                    new RoomOpening[]{RoomOpening.NORTH});
                        } else {
                            roomX = getRandomRoomByOpenings(counterOpeningsX);
                        }

                    }

                }
                copier = new Copier(roomX.getPivot(), roomX.getMin(), roomX.getMax());
                copier.paste(startLocationX);

                startRoomX = roomX;
            }

            startLocation = startLocation.subtract(0,0, roomSize);

        }
    }

    private Room getRandomRoomByOpenings(RoomOpening... openings) {
        List<Room> selectedRooms = new ArrayList<>();
        for(Room room : rooms) {
            boolean found = true;
            for(RoomOpening opening : openings) {
                if(!room.getOpenings().contains(opening)) {
                    found = false;
                }
            }
            if(found) {
                selectedRooms.add(room);
            }
        }
        return selectedRooms.get(MathUtil.random(selectedRooms.size() - 1));
    }

    private Room getRandomRoomByOpeningsAndNot(RoomOpening[] openings, RoomOpening[] notOpenings) {
        List<Room> selectedRooms = new ArrayList<>();
        for(Room room : rooms) {
            boolean found = true;
            for(RoomOpening opening : openings) {
                if(!room.getOpenings().contains(opening)) {
                    found = false;
                    break;
                }
            }
            for(RoomOpening opening : notOpenings) {
                if(room.getOpenings().contains(opening)) {
                    found = false;
                    break;
                }
            }
            if(found) {
                selectedRooms.add(room);
            }
        }
        return selectedRooms.get(MathUtil.random(selectedRooms.size() - 1));
    }
}
