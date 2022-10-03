package gg.maga.backrooms.game.participant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class GameParticipantRegistry {

    private Map<UUID, GameParticipant> participants;

    public GameParticipantRegistry() {
        this.participants = new HashMap<>();
    }

    public void register(UUID uuid, GameParticipant participant) {
        participants.put(uuid, participant);
    }

    public void unregister(UUID uuid) {
        participants.remove(uuid);
    }

    public boolean existsParticipant(UUID uuid) {
        return participants.containsKey(uuid);
    }

    public <T extends GameParticipant> Optional<T> getParticipantOptional(UUID uuid) {
        if(existsParticipant(uuid)) {
            return Optional.empty();
        }
        return Optional.of(getParticipant(uuid));
    }

    public <T extends GameParticipant> T getParticipant(UUID uuid) {
        return (T) participants.get(uuid);
    }
}
