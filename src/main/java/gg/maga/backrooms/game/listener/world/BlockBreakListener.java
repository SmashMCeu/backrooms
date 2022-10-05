package gg.maga.backrooms.game.listener.world;

import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.GameProvider;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class BlockBreakListener implements Listener {

    @Inject
    private GameMatchmaker matchmaker;

    @EventHandler
    public void onCall(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(matchmaker.isInGame(player)) {
            event.setCancelled(true);
        }
    }
}
