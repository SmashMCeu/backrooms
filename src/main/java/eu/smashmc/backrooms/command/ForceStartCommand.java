package eu.smashmc.backrooms.command;

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
public class ForceStartCommand extends PlayerCommand {

    public ForceStartCommand() {
        super("forcestart");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.performCommand("backrooms forcestart");
        return true;
    }
}
