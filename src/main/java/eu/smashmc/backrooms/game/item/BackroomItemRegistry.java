package eu.smashmc.backrooms.game.item;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.game.item.impl.*;
import eu.smashmc.backrooms.util.meta.RegisterMetaProcessor;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import eu.smashmc.backrooms.util.meta.Register;
import eu.smashmc.backrooms.util.meta.Register;

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

    public BackroomItemRegistry(Backrooms backrooms) {
        this.items = new HashMap<>();
        MetaRegistry metaRegistry = backrooms.getMetaRegistry();
        metaRegistry.registerProcessor(Register.class, new RegisterMetaProcessor(metaRegistry,
                instance -> {
                    if(instance instanceof BackroomItem item) {
                        register(item);
                        System.out.println("Registered item " + item.getId());
                    }
                }));
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
