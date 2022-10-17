package gg.maga.backrooms.game.item.impl;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.item.BackroomItem;
import gg.maga.backrooms.game.item.event.BackroomItemEvent;
import gg.maga.backrooms.game.model.Game;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AlmondWaterBackroomItem extends BackroomItem {

    public AlmondWaterBackroomItem() {
        super("Almond Water", Material.POTION);
        setDisplayName("§aAlmond Water");
        addLore("§7Heals half of your life");
    }

    @BackroomItemEvent
    public void onInteract(Player player, GameProvider provider, GameService service,
                           Game game, PlayerInteractEvent event) {
        double nextHealth = player.getHealth() + 10;
        if(nextHealth > 20) {
            nextHealth = 20;
        }
        player.setHealth(nextHealth);
        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_DRINK, 0.8f, 1);

        if (event.getHand() == EquipmentSlot.HAND) {
            player.getInventory().setItemInMainHand(null);
        } else if(event.getHand() == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(null);
        }
        player.updateInventory();
    }
}
