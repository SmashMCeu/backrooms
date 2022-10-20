package eu.smashmc.backrooms.game.listener.player;

import eu.smashmc.backrooms.game.GameService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    private GameService service;

    @EventHandler
    public void onCall(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(service.isInGame(player)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}
