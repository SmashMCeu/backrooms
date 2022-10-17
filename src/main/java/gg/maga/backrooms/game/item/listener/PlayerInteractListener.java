package gg.maga.backrooms.game.item.listener;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.item.BackroomItem;
import gg.maga.backrooms.game.item.BackroomItemRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractListener implements Listener {

    @Inject
    private BackroomItemRegistry itemRegistry;

    @Inject
    private GameService service;

    @Inject
    private GameProvider provider;

    @EventHandler
    public void onCall(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getItem() != null) {
            if(event.getItem().hasItemMeta()) {
                if(event.getItem().getItemMeta().hasDisplayName()) {
                    Optional<Game> optional = service.getGameByPlayer(player);
                    if(!optional.isPresent()) {
                        return;
                    }
                    Game game = optional.get();
                    for(BackroomItem item : itemRegistry.getItems().values()) {
                        if(item.getDisplayName().equals(event.getItem().getItemMeta().getDisplayName())) {
                            event.setCancelled(true);
                            item.getEventBus().publish(player, provider, service,game, event);
                        }
                    }
                }
            }
        }
    }
}
