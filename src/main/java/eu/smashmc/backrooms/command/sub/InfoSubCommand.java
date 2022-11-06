package eu.smashmc.backrooms.command.sub;

import com.google.common.base.Joiner;
import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.generator.room.PlacedRoom;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class InfoSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public InfoSubCommand(Backrooms backrooms) {
        super("info");
        setDescription("Info about current game");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "info");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        GameService service = backrooms.getGameProvider().getService();
        Optional<Game> optional = service.getGameByPlayer(player);
        if(optional.isPresent()) {
            Game game = optional.get();
            player.sendMessage(" ");
            player.sendMessage(BackroomsConstants.PREFIX + "§7ID: §a" + game.getId());
            player.sendMessage(BackroomsConstants.PREFIX + "§7Participants: §a" + game.getParticipantRegistry().getCount());
            player.sendMessage(BackroomsConstants.PREFIX + "§7Map: §a");
            player.sendMessage(BackroomsConstants.PREFIX + "   §7Chests: §a" + game.getMap().getChests().size());
            player.sendMessage(BackroomsConstants.PREFIX + "   §7Scientist Spawns: §a" + game.getMap().getScientistSpawns().size());
            player.sendMessage(BackroomsConstants.PREFIX + "   §7Entity Spawns: §a" + game.getMap().getEntitySpawns().size());
            player.sendMessage(BackroomsConstants.PREFIX + "   §7Portal blocks: §a" + game.getMap().getPortalBlocks().size());
            player.sendMessage(BackroomsConstants.PREFIX + "   §7Placed rooms: §a" + game.getMap().getResult().getRooms().size());
            List<String> enforcedRooms = game.getMap().getResult().getRooms().stream().
                    filter(placedRoom -> placedRoom.getRoom().hasName()).
                    map(room -> room.getRoom().getName()).collect(Collectors.toList());
            player.sendMessage(BackroomsConstants.PREFIX + "   §7Enforced rooms: §a"  + Joiner.on("§8, ").join(enforcedRooms));
            player.sendMessage(" ");
        } else {
            player.sendMessage(BackroomsConstants.PREFIX + "§cYou are not in a game.");
        }
        return true;
    }
}
