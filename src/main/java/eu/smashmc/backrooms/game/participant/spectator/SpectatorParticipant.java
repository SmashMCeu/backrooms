package eu.smashmc.backrooms.game.participant.spectator;

import eu.smashmc.api.SmashMc;
import eu.smashmc.api.vanish.Vanish;
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
    private Vanish<Player> vanish;

    public SpectatorParticipant(Player player) {
        super(player, "§7");
        this.vanish = SmashMc.getComponent(Vanish.class);
    }

    @Override
    public void onUpdate(GameProvider provider, GameService service, Game game) {
        if(vanish.isVanished(getPlayer())) {
            return;
        }
        spectating = service.spectate(getPlayer(), game, spectating);
    }
}
