package eu.smashmc.backrooms.game.loot.listener;

import eu.smashmc.backrooms.game.loot.model.ChestLoot;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.loot.ChestLootRegistry;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    private ChestLootRegistry registry;

    @Inject
    private GameService service;


    @EventHandler
    public void onCall(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getClickedBlock() != null) {
            if(event.getClickedBlock().getType() == Material.CHEST) {
                Optional<Game> optional = service.getGameByPlayer(player);
                if(!optional.isPresent()) {
                    return;
                }
                Game game = optional.get();
                if(game.getState() != GameState.IN_GAME) {
                    return;
                }
                GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                if(participant instanceof ScientistParticipant) {
                    Optional<ChestLoot> chestLootOptional = registry.findRandomLoot();
                    if(chestLootOptional.isPresent()) {
                        ChestLoot loot = chestLootOptional.get();
                        loot.give(event.getClickedBlock().getLocation(), player);
                        player.getWorld().playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_WOOD_BREAK, 0.5F, 1);
                        event.getClickedBlock().setType(Material.AIR);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
