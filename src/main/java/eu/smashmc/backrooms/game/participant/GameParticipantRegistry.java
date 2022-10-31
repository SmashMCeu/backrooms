package eu.smashmc.backrooms.game.participant;

import eu.smashmc.api.SmashComponent;
import eu.smashmc.api.SmashMc;
import eu.smashmc.api.vanish.Vanish;
import lombok.Getter;
import org.bukkit.entity.Player;

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

    public int getCount() {
        Vanish<Player> vanish = SmashMc.getComponent(Vanish.class);
        int count = 0;
        for(GameParticipant participant : participants.values()) {
            if(!vanish.isVanished(participant.getPlayer())) {
                count++;
            }
        }
        return count;
    }

    public void register(UUID uuid, GameParticipant participant) {
        participants.put(uuid, participant);
    }

    public GameParticipant unregister(UUID uuid) {
        GameParticipant participant = getParticipant(uuid);
        participants.remove(uuid);
        return participant;
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
