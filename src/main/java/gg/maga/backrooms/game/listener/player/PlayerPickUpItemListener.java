package gg.maga.backrooms.game.listener.player;

import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerPickUpItemListener implements Listener {

    @Inject
    private GameProvider provider;

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player player) {
            Optional<Game> optional = service.getGameByPlayer(player);
            if(optional.isPresent()) {
                Game game = optional.get();
                GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                if(participant instanceof ScientistParticipant scientist) {
                    int size = 0;
                    for(ItemStack stack : player.getInventory()) {
                        if(stack != null) {
                            size += stack.getAmount();
                        }
                    }
                    if(size >= 2) {
                        event.setCancelled(true);
                    }
                    return;
                }
                event.setCancelled(true);
            }
        }
    }
}
