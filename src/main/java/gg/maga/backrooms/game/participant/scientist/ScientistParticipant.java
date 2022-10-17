package gg.maga.backrooms.game.participant.scientist;

import gg.maga.backrooms.game.participant.GameParticipant;
import in.prismar.library.spigot.hologram.Hologram;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class ScientistParticipant extends GameParticipant {

    private ScientistState state;
    private int knocks;
    private Location knockedLocation;
    private long knockedUntil;
    private Hologram knockedHologram;

    private ScientistParticipant spectating;

    public ScientistParticipant(Player player) {
        super(player);
        this.knocks = 2;
        this.state = ScientistState.ALIVE;
    }


}
