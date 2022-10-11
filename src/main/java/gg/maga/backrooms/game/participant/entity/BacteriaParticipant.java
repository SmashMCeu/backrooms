package gg.maga.backrooms.game.participant.entity;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
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
        DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.ENDERMAN));

    }

}
