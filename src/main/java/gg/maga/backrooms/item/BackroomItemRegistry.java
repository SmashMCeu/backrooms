package gg.maga.backrooms.item;

import gg.maga.backrooms.item.holder.BackroomItemHolder;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private Map<UUID, List<BackroomItemHolder>> holders;

    public BackroomItemRegistry() {
        this.items = new HashMap<>();
        this.holders = new HashMap<>();
    }

    public void registerHolder(UUID uuid) {

    }

    public void unregisterHolder(UUID uuid) {

    }

    public void addHoldingItem(UUID uuid, BackroomItemHolder holder) {

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
}
