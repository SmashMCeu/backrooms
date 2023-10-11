package eu.smashmc.backrooms.game.loot;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.game.item.BackroomItemRegistry;
import eu.smashmc.backrooms.game.loot.model.ChestLoot;
import eu.smashmc.backrooms.game.loot.model.ItemChestLoot;
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

    public ChestLootRegistry(Backrooms backrooms) {
        this.loot = new ArrayList<>();
    }

    @SafeInitialize
    private void initialize() {
        register(new ItemChestLoot(0.4, itemRegistry.createItem("Almond Water")));
        register(new ItemChestLoot(0.3, itemRegistry.createItem("Adrenaline")));
        register(new ItemChestLoot(0.2, itemRegistry.createItem("Flashlight")));
        register(new ItemChestLoot(0.2, itemRegistry.createItem("Medkit")));
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
