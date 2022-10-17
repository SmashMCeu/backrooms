package gg.maga.backrooms.listener;

import gg.maga.backrooms.config.ConfigProvider;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
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
public class BlockPlaceListener implements Listener {

    @Inject
    private ConfigProvider provider;

    @EventHandler
    public void onCall(BlockPlaceEvent event) {
        if(!provider.getEntity().isBuild()) {
            event.setCancelled(true);
        }

    }
}
