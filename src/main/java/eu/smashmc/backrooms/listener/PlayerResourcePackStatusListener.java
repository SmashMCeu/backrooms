package eu.smashmc.backrooms.listener;

import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerResourcePackStatusListener implements Listener {

    @EventHandler
    public void onCall(PlayerResourcePackStatusEvent event) {
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            event.getPlayer().kickPlayer("§cYou must accept the resourcepack to play the game. Check your settings in your server list.");
        }
    }
}
