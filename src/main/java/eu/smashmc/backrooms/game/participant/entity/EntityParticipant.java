package eu.smashmc.backrooms.game.participant.entity;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class EntityParticipant extends GameParticipant {

    private final String name;

    public EntityParticipant(Player player, String name) {
        super(player, "§4");
        this.name = name;
        disableJump();
    }

    public void stun(GameProvider provider, GameService service, Game game, boolean blindness) {}
    public void onSeeScientist(GameProvider provider, GameService service, Game game, ScientistParticipant scientist) {}
    public void onScientistSee(GameProvider provider, GameService service, Game game, ScientistParticipant scientist) {}
    public void onAttackTarget(GameProvider provider, GameService service, Game game, ScientistParticipant target, EntityDamageByEntityEvent event) {}
}
