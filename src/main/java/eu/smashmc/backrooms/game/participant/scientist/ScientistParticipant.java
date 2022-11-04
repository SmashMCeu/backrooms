package eu.smashmc.backrooms.game.participant.scientist;

import eu.smashmc.backrooms.config.model.participant.ParticipantConfig;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import in.prismar.library.spigot.hologram.Hologram;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
        super(player, "§e");
        this.knocks = 2;
        this.state = ScientistState.ALIVE;

        disableJump();
    }

    @Override
    public void onUpdate(GameProvider provider, GameService service, Game game) {
        ParticipantConfig config = provider.getConfigProvider().getEntity().getParticipant();
        getPlayer().setWalkSpeed(config.getScientist().getNormalWalkSpeed());
        getPlayer().setFoodLevel(2);
        if(getState() == ScientistState.DEAD) {
            spectating = service.spectate(getPlayer(), game, spectating);
        }

    }
}
