package gg.maga.backrooms.game.participant.entity;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistState;
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
        super(player);
        this.name = name;
    }

    public void stun(GameProvider provider, GameService service, Game game, boolean blindness) {}
    public void onScientistSee(GameProvider provider, GameService service, Game game, ScientistParticipant scientist) {}
    public void onAttackTarget(GameProvider provider, GameService service, Game game, ScientistParticipant target, EntityDamageByEntityEvent event) {}
}
