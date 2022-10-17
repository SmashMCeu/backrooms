package gg.maga.backrooms.listener;

import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onCall(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
