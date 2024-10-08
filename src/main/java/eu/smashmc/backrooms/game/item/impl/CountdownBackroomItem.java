package eu.smashmc.backrooms.game.item.impl;

import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.item.event.BackroomItemEvent;
import eu.smashmc.backrooms.game.model.Game;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class CountdownBackroomItem extends BackroomItem {

    private Map<UUID, CountdownEntry> entries = new HashMap<>();

    private final int max;

    public CountdownBackroomItem(String id, Material material, int max) {
        super(id, material);
        this.max = max;
    }

    @BackroomItemEvent
    public void onInteract(Player player, GameProvider provider, GameService service,
                           Game game, PlayerInteractEvent event) {
        if(entries.containsKey(player.getUniqueId())) {
            CountdownEntry entry = entries.get(player.getUniqueId());
            return;
        }
        onCountdownInteract(player, provider, service, game, event);

        BukkitTask task = new BukkitRunnable() {
            ItemStack item = event.getItem();
            @Override
            public void run() {
                if(!entries.containsKey(player.getUniqueId())) {
                    cancel();
                    return;
                }
                CountdownEntry entry = entries.get(player.getUniqueId());
                if(entry.getCount() <= 0) {
                    entries.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                if(entry.getCount() >= 64) {
                    item.setAmount(64);
                } else {
                    item.setAmount(entry.getCount());
                }
                entry.setCount(entry.getCount() - 1);
                player.updateInventory();
            }
        }.runTaskTimer(provider.getBackrooms(), 20, 20);
        entries.put(player.getUniqueId(), new CountdownEntry(task, max));
    }

    public abstract void onCountdownInteract(Player player, GameProvider provider, GameService service, Game game, PlayerInteractEvent event);


    @Getter
    @Setter
    public class CountdownEntry {

        private final BukkitTask task;
        private final int max;
        private int count;

        public CountdownEntry(BukkitTask task, int max) {
            this.task = task;
            this.max = max;
            this.count = max;
        }
    }

}
