package eu.smashmc.backrooms.game.item.holder;

import eu.smashmc.backrooms.game.item.BackroomItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class BackroomItemHolder {

    private BackroomItem item;
    private ItemStack stack;
}
