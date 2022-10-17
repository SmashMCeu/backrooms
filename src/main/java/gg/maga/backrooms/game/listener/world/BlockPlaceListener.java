package gg.maga.backrooms.game.listener.world;

import gg.maga.backrooms.game.GameService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class BlockPlaceListener implements Listener {

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(service.isInGame(player)) {
            event.setCancelled(true);
        }
    }
}
