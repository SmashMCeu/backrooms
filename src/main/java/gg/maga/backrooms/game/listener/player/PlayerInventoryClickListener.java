package gg.maga.backrooms.game.listener.player;

import gg.maga.backrooms.game.GameMatchmaker;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInventoryClickListener implements Listener {

    @Inject
    private GameMatchmaker matchmaker;

    @EventHandler
    public void onCall(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(matchmaker.isInGame(player)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}
