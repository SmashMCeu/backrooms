package eu.smashmc.backrooms.util;

import eu.smashmc.lib.bukkit.server.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GameStateHack extends GameState implements Listener {

    private final Plugin plugin;

    public GameStateHack(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void update() {

    }

    @EventHandler
    public void onMotd(ServerListPingEvent event) {
        event.setMotd(this.getStatus().toJson());
    }
}
