package gg.maga.backrooms.generator.strategy.impl;

import gg.maga.backrooms.generator.BackroomsGenerator;
import gg.maga.backrooms.generator.strategy.AbstractBackroomsStrategy;
import gg.maga.backrooms.generator.strategy.result.GenerationResult;
import gg.maga.backrooms.room.Room;
import gg.maga.backrooms.room.RoomOpening;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.location.copier.Copier;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PrototypeBackroomsStrategy extends AbstractBackroomsStrategy<GenerationResult> {

    private final double roomSize;

    public PrototypeBackroomsStrategy(BackroomsGenerator generator, double roomSize) {
        super(generator);
        this.roomSize = roomSize;
    }
    
    @Override
    public CompletableFuture<GenerationResult> generate(Location location, int amount) {
        CompletableFuture<GenerationResult> future = new CompletableFuture<>();
        long now = System.currentTimeMillis();

        Room startRoom = null;
        Location startLocation = location.clone();

        for (int i = 0; i < amount; i++) {
            RoomOpening[] counterOpenings = i == 0 ? new RoomOpening[]{} :
                    startRoom.getOpenings().toArray(new RoomOpening[0]);
            for (int j = 0; j < counterOpenings.length; j++) {
                counterOpenings[j] = RoomOpening.valueOf(counterOpenings[j].getCounterOpening());
            }

            Room room;

            if(i == 0) {
                room = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.NORTH, RoomOpening.EAST},
                        new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.WEST});
            } else if(i == amount - 1) {
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

            for (int j = 0; j < amount; j++) {
                startLocationX = startLocationX.add(roomSize, 0, 0);

                RoomOpening[] counterOpeningsX = startRoomX.getOpenings().toArray(new RoomOpening[0]);
                for (int k = 0; k < counterOpeningsX.length; k++) {
                    counterOpeningsX[k] = RoomOpening.valueOf(counterOpeningsX[k].getCounterOpening());
                }

                Room roomX;
                if(i == 0) {
                    if (j == amount - 1) {
                        roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.WEST, RoomOpening.NORTH},
                                new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.EAST});
                    } else {
                        roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH, RoomOpening.WEST},
                                new RoomOpening[]{RoomOpening.SOUTH});
                    }
                } else {
                    if(j == amount - 1) {
                        if(i == amount - 1) {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.WEST, RoomOpening.SOUTH},
                                    new RoomOpening[]{RoomOpening.EAST, RoomOpening.NORTH});
                        } else {
                            roomX = getRandomRoomByOpeningsAndNot(new RoomOpening[]{RoomOpening.SOUTH, RoomOpening.NORTH, RoomOpening.WEST},
                                    new RoomOpening[]{RoomOpening.EAST});
                        }

                    } else {
                        if(i == amount - 1) {
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
        
        
        long time = System.currentTimeMillis() - now;
        future.complete(new GenerationResult(time));
        return future;
    }

    private Room getRandomRoomByOpenings(RoomOpening... openings) {
        List<Room> selectedRooms = new ArrayList<>();
        for(Room room : getGenerator().getRooms().values()) {
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
        for(Room room : getGenerator().getRooms().values()) {
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
