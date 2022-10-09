package gg.maga.backrooms.game.participant.entity;

import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BacteriaParticipant extends EntityParticipant {

    private static final float WALK_SPEED = 0.25f;

    public BacteriaParticipant(Player player) {
        super(player, "Bacteria");
        player.setWalkSpeed(WALK_SPEED);
    }

}
