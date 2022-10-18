package gg.maga.backrooms.game.participant.scientist;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import in.prismar.library.spigot.hologram.Hologram;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        disableJump();
    }

    @Override
    public void onUpdate(GameProvider provider, GameService service, Game game) {
        if(getState() == ScientistState.DEAD) {
            if (getPlayer().getSpectatorTarget() == null) {
                getPlayer().setGameMode(GameMode.SPECTATOR);

                int alive = service.getAliveParticipants(game);
                if (alive >= 1) {
                    ScientistParticipant random = service.findRandomScientist(game);
                    setSpectating(random);
                    getPlayer().setSpectatorTarget(random.getPlayer());
                }
            } else {
                if (getSpectating().getState() != ScientistState.ALIVE) {
                    ScientistParticipant random = service.findRandomScientist(game);
                    setSpectating(random);
                    getPlayer().setSpectatorTarget(random.getPlayer());
                }
            }
        }

    }
}
