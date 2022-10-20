package eu.smashmc.backrooms.game.item.listener;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.item.BackroomItemRegistry;
import eu.smashmc.backrooms.game.model.Game;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerConsumeItemListener implements Listener {

    @Inject
    private BackroomItemRegistry itemRegistry;

    @Inject
    private GameService service;

    @Inject
    private GameProvider provider;

    @EventHandler
    public void onCall(PlayerItemConsumeEvent event) {
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
                            item.getEventBus().publish(player, provider, service,game, event);
                        }
                    }
                }
            }
        }
    }
}
