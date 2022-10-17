package gg.maga.backrooms.game.listener.player;

import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractListener implements Listener {

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
                    GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                    if(participant instanceof ScientistParticipant) {
                        service.solveTask(game);
                    } else {
                        event.setCancelled(true);
                    }

                }
            }

        }
    }
}
