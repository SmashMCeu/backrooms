package eu.smashmc.backrooms.game.participant.spectator;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class SpectatorParticipant extends GameParticipant {

    private ScientistParticipant spectating;

    public SpectatorParticipant(Player player) {
        super(player, "§7");
    }

    @Override
    public void onUpdate(GameProvider provider, GameService service, Game game) {
        spectating = service.spectate(getPlayer(), game, spectating);
    }
}
