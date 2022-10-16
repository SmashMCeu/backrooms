package gg.maga.backrooms.game.loot.model;

import gg.maga.backrooms.BackroomsConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class ItemChestLoot extends AbstractChestLoot {

    private final ItemStack item;

    public ItemChestLoot(double chance, ItemStack item) {
        super(chance);
        this.item = item;
    }

    @Override
    public void give(Location chest, Player player) {
        chest.getWorld().dropItem(chest, item.clone());
        player.sendMessage(BackroomsConstants.PREFIX + "§7You received §a" + item.getItemMeta().getDisplayName());
    }
}
