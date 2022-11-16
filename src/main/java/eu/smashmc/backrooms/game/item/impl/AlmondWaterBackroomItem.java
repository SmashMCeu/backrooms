package eu.smashmc.backrooms.game.item.impl;

import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.item.event.BackroomItemEvent;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.util.meta.Register;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Register
public class AlmondWaterBackroomItem extends BackroomItem {

    public AlmondWaterBackroomItem() {
        super("Almond Water", Material.POTION);
        setDisplayName("§aAlmond Water");
        addLore("§7Heals half of your life");
    }

    @BackroomItemEvent
    public void onInteract(Player player, GameProvider provider, GameService service,
                          Game game, PlayerInteractEvent event) {
        event.setCancelled(false);
    }

    @BackroomItemEvent
    public void onConsume(Player player, GameProvider provider, GameService service,
                           Game game, PlayerItemConsumeEvent event) {
        double nextHealth = player.getHealth() + 10;
        if(nextHealth > 20) {
            nextHealth = 20;
        }
        event.setCancelled(true);
        player.setHealth(nextHealth);
        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_DRINK, 0.8f, 1);
        if(event.getHand() == EquipmentSlot.HAND) {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        } else if(event.getHand() == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        }
        player.updateInventory();
    }
}
