package eu.smashmc.backrooms.command.sub;

import eu.smashmc.backrooms.generator.room.PlacedRoom;
import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GenerateSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public GenerateSubCommand(Backrooms backrooms) {
        super("generate");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "generate");
        setDescription("Generate a backroom");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(BackroomsConstants.PREFIX + "§7Start generating backrooms...");
        backrooms.getGameProvider().prepareGame().thenAccept(game -> {
            player.sendMessage(BackroomsConstants.PREFIX + "§7ID: §a" + game.getId());
            player.sendMessage(BackroomsConstants.PREFIX + "§7Scientist spawns: §a" + game.getMap().getScientistSpawns().size());
            player.sendMessage(BackroomsConstants.PREFIX + "§7Entity spawns: §a" + game.getMap().getEntitySpawns().size());

            int customRooms = 0;
            for(PlacedRoom room : game.getMap().getResult().getRooms()) {
                if(room.getRoom().getAmount() >= 1) {
                    customRooms++;
                }
            }
            player.sendMessage(BackroomsConstants.PREFIX + "§7Custom rooms: §a" + customRooms);
            player.sendMessage(BackroomsConstants.PREFIX + "§7Chests: §a" + game.getMap().getChests().size());

            player.sendMessage(BackroomsConstants.PREFIX + "§aSuccessfully finished generating backrooms");

        });
        return true;
    }
}
