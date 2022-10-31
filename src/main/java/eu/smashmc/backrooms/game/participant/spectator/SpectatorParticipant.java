package eu.smashmc.backrooms.game.participant.spectator;

import eu.smashmc.backrooms.game.participant.GameParticipant;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpectatorParticipant extends GameParticipant {

    public SpectatorParticipant(Player player) {
        super(player, "§7");
    }
}
