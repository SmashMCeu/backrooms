package eu.smashmc.backrooms.command.sub;

import eu.smashmc.backrooms.generator.room.Room;
import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ScanSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public ScanSubCommand(Backrooms backrooms) {
        super("scan");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "scan");
        setDescription("Scan all rooms");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(BackroomsConstants.PREFIX + "§7Start scanning backrooms...");
        List<Room> rooms = backrooms.getScanner().scan();
        player.sendMessage(BackroomsConstants.PREFIX + "§7Found §a" + rooms.size() + " §7rooms");
        backrooms.getGenerator().setRooms(rooms);
        return true;
    }
}
