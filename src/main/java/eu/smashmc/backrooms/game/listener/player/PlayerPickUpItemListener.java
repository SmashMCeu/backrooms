package eu.smashmc.backrooms.game.listener.player;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
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
                            if(stack.getType() != Material.AIR) {
                                size += stack.getAmount();
                            }
                        }
                    }
                    event.setCancelled(true);
                    if(size >= 2) {
                        return;
                    }
                    ItemStack stack = event.getItem().getItemStack();
                    event.getItem().remove();

                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        ItemStack item = player.getInventory().getItem(i);
                        if(item == null) {
                            player.getInventory().setItem(i, stack);
                            return;
                        }
                        if(item.getType() == Material.AIR) {
                            player.getInventory().setItem(i, stack);
                            return;
                        }
                    }

                    return;
                }
                event.setCancelled(true);
            }
        }
    }
}
