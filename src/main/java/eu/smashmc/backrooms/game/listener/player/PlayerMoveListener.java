package eu.smashmc.backrooms.game.listener.player;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
import eu.smashmc.lib.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.location.LocationUtil;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerMoveListener implements Listener {

    @Inject
    private GameProvider provider;

    @Inject
    private GameService service;

    @EventHandler
    public void onCall(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().clone().add(0, 1, 0).getBlock();
        if(block.getType() == Material.NETHER_PORTAL) {
            if(service.isInGame(player)) {
                Game game = service.getGameByPlayer(player).get();
                GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                if(participant instanceof ScientistParticipant scientist) {
                    if(scientist.getState() != ScientistState.ESCAPED) {
                        service.escape(game, scientist);
                    }
                }
            }
            return;
        }
        Optional<Game> optional = service.getGameByPlayer(player);
        if(optional.isPresent()) {
            Game game = optional.get();
            if(game.getState() == GameState.IN_GAME) {
                GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
                Location from = event.getFrom();
                Location to = event.getTo();
                if(participant instanceof EntityParticipant entity) {
                    if(from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                        for(GameParticipant targetParticipant : service.raytraceParticipants(game, player)) {
                            if(targetParticipant instanceof ScientistParticipant scientist) {
                                if(scientist.getState() == ScientistState.ALIVE) {
                                    entity.onSeeScientist(provider, service, game, scientist);
                                    break;
                                }

                            }
                        }
                    }
                }


                double deathY = provider.getGenerationLocation().getY() - provider.getConfigProvider().getEntity().getGame().getHoleTeleportHeight();
                if(player.getLocation().getY() <= deathY) {
                    Location location = findRandomMapLocation(game);
                    player.teleport(location);

                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.7f, 1);


                    if(participant instanceof ScientistParticipant scientist) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 0.6f, 1);
                        if(player.getHealth() <= 12) {
                            service.knock(game, null, scientist);
                        } else {
                            player.setHealth(10);
                        }
                    }
                    return;
                }
            }

        }

    }

    private Location findRandomMapLocation(Game game) {
        Location spot = findRandomSpot(game);
        while (spot.getBlock().getType() != Material.AIR || spot.getBlock().getLightLevel() < 0) {
            spot = findRandomSpot(game);
        }
        spot = LocationUtil.getCenterOfBlock(spot);
        return spot;
    }

    private Location findRandomSpot(Game game) {
        World world = game.getMap().getMin().getWorld();
        Location search = new Location(world, MathUtil.random(game.getMap().getMin().getX(), game.getMap().getMax().getX()),
                provider.getGenerationLocation().getY() + 1.5, MathUtil.random(game.getMap().getMin().getZ(), game.getMap().getMax().getZ()));
        return search;
    }
}
