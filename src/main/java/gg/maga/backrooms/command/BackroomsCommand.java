package gg.maga.backrooms.command;

import com.google.common.base.Joiner;
import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.room.Room;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class BackroomsCommand extends PlayerCommand {

    private final Backrooms backrooms;

    public BackroomsCommand(Backrooms backrooms) {
        super("backrooms");
        setAliases("br", "backroom");

        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 1) {
            player.sendMessage(BackroomsConstants.PREFIX + "§7Scanning rooms...");
            backrooms.getGenerator().setRooms(backrooms.getScanner().scan());
            player.sendMessage(BackroomsConstants.PREFIX + "§7Found §a" + backrooms.getGenerator().getRooms().size() + " rooms");
            player.sendMessage(BackroomsConstants.PREFIX + "§7Start generating backrooms...");
            backrooms.getGenerator().generate(player.getLocation(), arguments.getInteger(0)).thenAccept(result -> {
                player.sendMessage(BackroomsConstants.PREFIX + "§aSuccessfully finished generating backrooms");
            });
            return true;
        }
        player.sendMessage(BackroomsConstants.PREFIX + "§cUsage: /backrooms <rooms>");
        return true;
    }

    private void printRoom(Player player, Room first) {
        player.sendMessage("Min: " + first.getMin().getBlockX() + " / " + first.getMin().getBlockY() + " / " + first.getMin().getBlockZ());
        player.sendMessage("Max: " + first.getMax().getBlockX() + " / " + first.getMax().getBlockY() + " / " + first.getMax().getBlockZ());
        player.sendMessage("Openings: " + Joiner.on(", ").join(first.getOpenings()));
    }
}
