package eu.smashmc.backrooms.game.listener.player;

import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractListener implements Listener {

    public static final Set<Location> USED_LEVERS = new HashSet<>();

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(service.isInGame(player)) {
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                if(block.getType() == Material.LEVER) {
                    Switch power = (Switch) block.getBlockData();
                    if(power.isPowered()) {
                       event.setCancelled(true);
                       return;
                    }
                    Game game = service.getGameByPlayer(player).get();
                    if(game.getState() != GameState.IN_GAME) {
                        return;
                    }
                    if(USED_LEVERS.contains(block.getLocation())) {
                        player.sendMessage(BackroomsConstants.PREFIX + "§7This lever is already activated.");
                        return;
                    }
                    GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                    if(participant instanceof ScientistParticipant) {
                        power.setPowered(true);
                        block.setBlockData(power);
                        USED_LEVERS.add(block.getLocation());
                        service.solveTask(game);
                    } else {
                        event.setCancelled(true);
                    }

                }
            }

        }
    }
}
