package gg.maga.backrooms.game.listener.player;

import gg.maga.backrooms.game.GameService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerFoodLevelChangeListener implements Listener {

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if(service.isInGame(player)) {
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }
}
