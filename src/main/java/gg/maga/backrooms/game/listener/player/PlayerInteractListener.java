package gg.maga.backrooms.game.listener.player;

import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import net.minecraft.world.level.block.LeverBlock;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractListener implements Listener {

    @Inject
    private GameMatchmaker matchmaker;

    @EventHandler
    public void onCall(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(matchmaker.isInGame(player)) {
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                if(block.getType() == Material.LEVER) {
                    Switch power = (Switch) block.getBlockData();
                    if(power.isPowered()) {
                       event.setCancelled(true);
                       return;
                    }
                    Game game = matchmaker.getGameByPlayer(player).get();
                    int tasks = game.getSolvedTasks() + 1;
                    if(tasks <= game.getProperties().getMaxTasks()) {
                        game.setSolvedTasks(tasks);
                        game.getProvider().getMatchmaker().executeForAll(game, participant -> {
                            participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.BLOCK_PISTON_EXTEND, 0.45F, 1);
                            participant.getPlayer().sendTitle("§aTask solved", "", 20, 20, 20);
                        });
                    }
                    if(tasks == game.getProperties().getMaxTasks()) {
                        game.getProvider().getMatchmaker().executeForAll(game, participant -> {
                            participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.45F, 1);
                            participant.getPlayer().sendTitle("§eThreshold", "§7has been opened", 20, 40, 20);
                        });
                        matchmaker.openThreshold(game);
                    }
                }
            }

        }
    }
}
