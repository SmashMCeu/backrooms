package gg.maga.backrooms.game.loot.listener;

import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.item.BackroomItem;
import gg.maga.backrooms.game.item.BackroomItemRegistry;
import gg.maga.backrooms.game.loot.ChestLootRegistry;
import gg.maga.backrooms.game.loot.model.ChestLoot;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
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
    private GameMatchmaker matchmaker;


    @EventHandler
    public void onCall(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getClickedBlock() != null) {
            if(event.getClickedBlock().getType() == Material.CHEST) {
                Optional<Game> optional = matchmaker.getGameByPlayer(player);
                if(!optional.isPresent()) {
                    return;
                }
                Game game = optional.get();
                if(game.getState() != GameState.IN_GAME) {
                    return;
                }
                Optional<ChestLoot> chestLootOptional = registry.findRandomLoot();
                if(chestLootOptional.isPresent()) {
                    ChestLoot loot = chestLootOptional.get();
                    loot.give(event.getClickedBlock().getLocation(), player);
                    player.getWorld().playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_WOOD_BREAK, 0.5F, 1);
                    event.getClickedBlock().setType(Material.AIR);
                }


            }
        }
    }
}
