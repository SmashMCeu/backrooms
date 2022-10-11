package gg.maga.backrooms.game.listener.entity;

import gg.maga.backrooms.game.GameMatchmaker;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityRegainHealthListener implements Listener {

    @Inject
    private GameMatchmaker matchmaker;

    @EventHandler
    public void onCall(EntityRegainHealthEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(matchmaker.isInGame(player)) {
                event.setCancelled(true);
            }
        }
    }
}
