package gg.maga.backrooms.game.item.impl;

import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.item.BackroomItem;
import gg.maga.backrooms.game.item.event.BackroomItemEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LeaveBackroomItem extends BackroomItem {

    public LeaveBackroomItem() {
        super("Leave", Material.OAK_DOOR);
        setDisplayName("§cLeave");
    }

    @BackroomItemEvent
    public void onInteract(Player player, Game game, PlayerInteractEvent event) {
        game.getProvider().getMatchmaker().leaveGame(game, player, false);
    }
}
