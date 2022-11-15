package eu.smashmc.backrooms.game.item.impl;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.item.event.BackroomItemEvent;
import eu.smashmc.backrooms.game.model.Game;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AdrenalineBackroomItem extends BackroomItem {

    public AdrenalineBackroomItem() {
        super("Adrenaline", Material.SUGAR);
        setDisplayName("§eAdrenaline");
        addLore("§7Let's you run faster.");
    }

    @BackroomItemEvent
    public void onInteract(Player player, GameProvider provider, GameService service,
                          Game game, PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.HAND) {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        } else if(event.getHand() == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*10, 1));
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.8f, 2);
        player.updateInventory();
        event.setCancelled(true);
    }
}
