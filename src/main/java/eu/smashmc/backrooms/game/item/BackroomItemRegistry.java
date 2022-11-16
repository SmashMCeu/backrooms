package eu.smashmc.backrooms.game.item;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.game.item.impl.*;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class BackroomItemRegistry {

    private Map<String, BackroomItem> items;

    public BackroomItemRegistry() {
        this.items = new HashMap<>();
        load();
    }

    private void load() {
        register(new AdrenalineBackroomItem());
        register(new AlmondWaterBackroomItem());
        register(new FlashlightBackroomItem());
        register(new LeaveBackroomItem());
        register(new RevealBackroomItem());
    }

    public void register(BackroomItem item) {
        this.items.put(item.getId(), item);
    }

    public BackroomItem getItemById(String id) {
        return this.items.get(id);
    }

    public boolean existsItemById(String id) {
        return this.items.containsKey(id);
    }

    public ItemStack createItem(String id) {
        return getItemById(id).build();
    }
}
