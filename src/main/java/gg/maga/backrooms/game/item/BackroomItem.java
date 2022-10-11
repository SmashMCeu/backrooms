package gg.maga.backrooms.game.item;

import gg.maga.backrooms.game.item.event.BackroomItemEventBus;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class BackroomItem {

    private final String id;
    private final Material material;

    private String displayName;
    private List<String> lore;

    private BackroomItemEventBus eventBus;

    public BackroomItem(String id, Material material) {
        this.id = id;
        this.material = material;
        this.eventBus = new BackroomItemEventBus(this);
    }

    public BackroomItem addLore(String... lore) {
        if(this.lore == null) {
            this.lore = new ArrayList<>();
        }
        this.lore.addAll(Arrays.asList(lore));
        return this;
    }

    public ItemStack build() {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if(displayName != null) {
            meta.setDisplayName(displayName);
        }
        if(lore != null) {
            meta.setLore(lore);
        }
        stack.setItemMeta(meta);
        return stack;
    }
}
