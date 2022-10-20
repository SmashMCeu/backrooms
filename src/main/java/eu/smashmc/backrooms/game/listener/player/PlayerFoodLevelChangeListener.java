package eu.smashmc.backrooms.game.listener.player;

import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
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
            Game game = service.getGameByPlayer(player).get();
            event.setCancelled(true);

        }
    }
}
