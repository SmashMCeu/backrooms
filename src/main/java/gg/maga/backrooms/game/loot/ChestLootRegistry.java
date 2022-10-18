package gg.maga.backrooms.game.loot;

import gg.maga.backrooms.game.item.BackroomItemRegistry;
import gg.maga.backrooms.game.loot.model.ChestLoot;
import gg.maga.backrooms.game.loot.model.ItemChestLoot;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class ChestLootRegistry {

    @Inject
    private BackroomItemRegistry itemRegistry;

    private List<ChestLoot> loot;

    public ChestLootRegistry() {
        this.loot = new ArrayList<>();
    }

    @SafeInitialize
    private void initialize() {
        register(new ItemChestLoot(0.4, itemRegistry.createItem("Almond Water")));
        register(new ItemChestLoot(0.2, itemRegistry.createItem("Flashlight")));
    }

    public void register(ChestLoot loot) {
        this.loot.add(loot);
    }

    public Optional<ChestLoot> findRandomLoot() {
        double sum = loot.stream().mapToDouble(ChestLoot::getChance).sum();
        double random = Math.random() * sum;
        for(ChestLoot loot : this.loot) {
            random -= loot.getChance();
            if(random < 0) {
                return Optional.of(loot);
            }
        }
        return Optional.empty();
    }
}
