package eu.smashmc.backrooms.generator.strategy.impl;

import eu.smashmc.backrooms.generator.BackroomsGenerator;
import eu.smashmc.backrooms.generator.room.PlacedRoom;
import eu.smashmc.backrooms.generator.room.Room;
import eu.smashmc.backrooms.generator.room.RoomOpening;
import eu.smashmc.backrooms.generator.strategy.AbstractBackroomsStrategy;
import eu.smashmc.backrooms.generator.strategy.result.GenerationResult;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.location.copier.Copier;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PrototypeBackroomsStrategy extends AbstractBackroomsStrategy<GenerationResult> {

    private final double roomSize;
    private final double halfRoomSize;

    public PrototypeBackroomsStrategy(BackroomsGenerator generator, double roomSize) {
        super(generator);
        this.roomSize = roomSize;
        this.halfRoomSize = Math.floor(roomSize / 2);
    }

    @Override
    public CompletableFuture<GenerationResult> generate(Location location, int amount) {
        CompletableFuture<GenerationResult> future = new CompletableFuture<>();
        List<PlacedRoom> placedRooms = new ArrayList<>();

        Map<Integer, Room> preplacedRooms = findPreplacedRooms(amount);

        long now = System.currentTimeMillis();

        Room startRoom = null;
        Location startLocation = location.clone();

        int index = 0;
        for (int i = 0; i < amount; i++) {
            RoomOpening[] counterOpenings = i == 0 ? new RoomOpening[]{} :
                    startRoom.getOpenings().toArray(new RoomOpening[0]);
            for (int j = 0; j < counterOpenings.length; j++) {
                counterOpenings[j] = RoomOpening.valueOf(counterOpenings[j].getCounterOpening());
            }

            Room room;

            if (i == 0) {
                room = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.NORTH, RoomOpening.EAST},
                        new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.WEST});
            } else if (i == amount - 1) {
                room = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.EAST},
                        new RoomOpening[]{RoomOpening.WEST, RoomOpening.NORTH});
            } else {
                room = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH, RoomOpening.SOUTH},
                        new RoomOpening[]{RoomOpening.WEST});
            }
            copy(room, startLocation);

            placedRooms.add(new PlacedRoom(room, startLocation.clone().subtract(halfRoomSize, 0, halfRoomSize)));

            startRoom = room;


            Location startLocationX = startLocation.clone();
            Room startRoomX = getRandomRoomByOpenings(counterOpenings);

            index++;

            for (int j = 0; j < amount; j++) {
                index++;
                startLocationX = startLocationX.add(roomSize, 0, 0);

                RoomOpening[] counterOpeningsX = startRoomX.getOpenings().toArray(new RoomOpening[0]);
                for (int k = 0; k < counterOpeningsX.length; k++) {
                    counterOpeningsX[k] = RoomOpening.valueOf(counterOpeningsX[k].getCounterOpening());
                }

                Room roomX;
                if (i == 0) {
                    if (j == amount - 1) {
                        roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.WEST, RoomOpening.NORTH},
                                new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.EAST});
                    } else {
                        roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH, RoomOpening.WEST},
                                new RoomOpening[]{RoomOpening.SOUTH});
                    }
                } else {
                    if (j == amount - 1) {
                        if (i == amount - 1) {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.WEST, RoomOpening.SOUTH},
                                    new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH});
                        } else {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.NORTH, RoomOpening.WEST},
                                    new RoomOpening[]{RoomOpening.EAST});
                        }

                    } else {
                        if (i == amount - 1) {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.WEST, RoomOpening.EAST},
                                    new RoomOpening[]{RoomOpening.NORTH});
                        } else {
                            if(preplacedRooms.containsKey(index)) {
                                roomX = preplacedRooms.get(index);
                            } else {
                                roomX = getRandomRoomByOpenings(counterOpeningsX);
                                while (containsPreplacedRoom(preplacedRooms, roomX)) {
                                    roomX = getRandomRoomByOpenings(counterOpeningsX);
                                }
                            }

                        }

                    }

                }
                copy(roomX, startLocationX);

                placedRooms.add(new PlacedRoom(roomX, startLocationX.clone().subtract(halfRoomSize, 0, halfRoomSize)));

                startRoomX = roomX;
            }

            startLocation = startLocation.subtract(0, 0, roomSize);

        }


        long time = System.currentTimeMillis() - now;
        future.complete(new GenerationResult(time, placedRooms));
        return future;
    }

    private boolean containsPreplacedRoom(Map<Integer, Room> preplacedRooms, Room room) {
        for(Room preRoom : preplacedRooms.values()) {
            if(preRoom.hasName() && room.hasName()) {
                if(preRoom.getName().equals(room.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Copier copy(Room room, Location location) {
        Copier copier = new Copier(room.getPivot(), room.getMin(), room.getMax());
        copier.paste(location);
        return copier;
    }

    private Map<Integer, Room> findPreplacedRooms(int amount) {
        Map<Integer, Room> rooms = new HashMap<>();
        List<Integer> allowedNumbers = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < amount; i++) {
            if (i != 0 && i != amount - 1) {
                allowedNumbers.add(index);
            }
            index++;
            for (int j = 0; j < amount; j++) {
                index++;
                if (i != 0) {
                    if (j != amount - 1) {
                        if (i != amount - 1) {
                            allowedNumbers.add(index);
                        }

                    }
                }
            }
        }
        for(Room room : getGenerator().getRooms()) {
            if(room.getAmount() >= 1) {
                for (int i = 0; i < room.getAmount(); i++) {
                    int random = allowedNumbers.get(MathUtil.random(allowedNumbers.size() - 1));
                    while (rooms.containsKey(random)) {
                        random = allowedNumbers.get(MathUtil.random(allowedNumbers.size() - 1));
                    }
                    rooms.put(random, room);

                }
            }
        }
        return rooms;
    }

    private Room getRandomRoomByOpenings(RoomOpening... openings) {
        List<Room> selectedRooms = new ArrayList<>();
        for (Room room : getGenerator().getRooms()) {
            boolean found = true;
            for (RoomOpening opening : openings) {
                if (!room.getOpenings().contains(opening)) {
                    found = false;
                }
            }
            if (found) {
                selectedRooms.add(room);
            }
        }
        return selectedRooms.get(getGenerator().getRandom().nextInt(selectedRooms.size()));
    }

    private Room getRandomRoomByOpeningsAndNot(RoomOpening[] openings, RoomOpening[] notOpenings) {
        List<Room> selectedRooms = new ArrayList<>();
        for (Room room : getGenerator().getRooms()) {
            boolean found = true;
            for (RoomOpening opening : openings) {
                if (!room.getOpenings().contains(opening)) {
                    found = false;
                    break;
                }
            }
            for (RoomOpening opening : notOpenings) {
                if (room.getOpenings().contains(opening)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                selectedRooms.add(room);
            }
        }
        return selectedRooms.get(getGenerator().getRandom().nextInt(selectedRooms.size()));
    }
}
