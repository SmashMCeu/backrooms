package eu.smashmc.backrooms.generator.strategy.impl;

import eu.smashmc.backrooms.generator.BackroomsGenerator;
import eu.smashmc.backrooms.generator.room.PlacedRoom;
import eu.smashmc.backrooms.generator.room.Room;
import eu.smashmc.backrooms.generator.room.RoomOpening;
import eu.smashmc.backrooms.generator.strategy.AbstractBackroomsStrategy;
import eu.smashmc.backrooms.generator.strategy.result.GenerationResult;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.tuple.Tuple;
import in.prismar.library.spigot.location.copier.Copier;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PrototypeV2BackroomsStrategy extends AbstractBackroomsStrategy<GenerationResult> {

    private final double roomSize;
    private final double halfRoomSize;

    private final double replaceLightBlockChance;
    private final Material lightBlock;
    private final Material replaceLightBlock;

    public PrototypeV2BackroomsStrategy(BackroomsGenerator generator, double roomSize, double replaceLightBlockChance, Material lightBlock, Material replaceLightBlock) {
        super(generator);
        this.roomSize = roomSize;
        this.halfRoomSize = Math.floor(roomSize / 2);
        this.replaceLightBlockChance = replaceLightBlockChance;
        this.lightBlock = lightBlock;
        this.replaceLightBlock = replaceLightBlock;
    }

    @Override
    public CompletableFuture<GenerationResult> generate(Location location, int amount) {
        CompletableFuture<GenerationResult> future = new CompletableFuture<>();
        List<PlacedRoom> placedRooms = new ArrayList<>();
        List<Room> preplacedRooms = findPreplacedRooms();

        long now = System.currentTimeMillis();

        Room startRoom = null;
        Location startLocation = location.clone();

        List<Tuple<Location, Integer>> universalRooms = new ArrayList<>();

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
                boolean universal = false;
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
                            roomX = getRandomRoomByOpenings(counterOpeningsX);
                            while (roomX.hasName()) {
                                roomX = getRandomRoomByOpenings(counterOpeningsX);
                            }
                            universal = true;
                        }

                    }
                }
                copy(roomX, startLocationX);
                if(!universal) {
                    placedRooms.add(new PlacedRoom(roomX, startLocationX.clone().subtract(halfRoomSize, 0, halfRoomSize)));
                } else {
                    placedRooms.add(new PlacedRoom(roomX, startLocationX.clone().subtract(halfRoomSize, 0, halfRoomSize)));
                    universalRooms.add(new Tuple<>(startLocationX, placedRooms.size() - 1));
                }



                startRoomX = roomX;
            }

            startLocation = startLocation.subtract(0, 0, roomSize);

        }
        for(Room room : preplacedRooms) {
            Tuple<Location, Integer> random = universalRooms.get(MathUtil.random(universalRooms.size() - 1));
            copy(room, random.getFirst());
            placedRooms.remove(random.getSecond());
            placedRooms.add(random.getSecond(), new PlacedRoom(room, random.getFirst().clone().subtract(halfRoomSize, 0, halfRoomSize)));
            universalRooms.remove(random);
        }

        long time = System.currentTimeMillis() - now;
        future.complete(new GenerationResult(time, placedRooms));
        return future;
    }




    private Copier copy(Room room, Location location) {
        Copier copier = new Copier(room.getPivot(), room.getMin(), room.getMax());

        double chance = Math.random();
        if(chance <= replaceLightBlockChance) {
            copier.setCallback((from, to) -> {
                if(from.getType() == lightBlock) {
                    to.setType(replaceLightBlock);
                    return true;
                }
                return false;
            });
        }

        copier.paste(location);
        return copier;
    }

    private List<Room> findPreplacedRooms() {
        List<Room> rooms = new ArrayList<>();
        for(Room room : getGenerator().getRooms()) {
            if(room.getAmount() >= 1) {
                for (int i = 0; i < room.getAmount(); i++) {
                    rooms.add(room);
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
