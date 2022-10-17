package gg.maga.backrooms.listener;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.config.ConfigProvider;
import gg.maga.backrooms.game.GameService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class BlockBreakListener implements Listener {

    @Inject
    private ConfigProvider provider;

    @EventHandler
    public void onCall(BlockBreakEvent event) {
        if(!provider.getEntity().isBuild()) {
            event.setCancelled(true);
        }

    }
}
