package eu.smashmc.backrooms.game.item.impl;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.item.event.BackroomItemEvent;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.lib.common.minecraft.MinecraftBridge;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LeaveBackroomItem extends BackroomItem {

    public LeaveBackroomItem() {
        super("Leave", Material.OAK_DOOR);
        setDisplayName("§cLeave");
    }

    @BackroomItemEvent
    public void onInteract(Player player, GameProvider provider, GameService service,
                           Game game, PlayerInteractEvent event) {
        sendServer(provider.getBackrooms(), player, "lobby");
    }

    public void sendServer(Plugin plugin, Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            out.close();
            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
