package gg.maga.backrooms.game.participant.scientist;

import gg.maga.backrooms.game.participant.GameParticipant;
import lombok.Getter;
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
public class ScientistParticipant extends GameParticipant {

    private ScientistState state;

    public ScientistParticipant(Player player) {
        super(player);
        this.state = ScientistState.ALIVE;
    }


}
