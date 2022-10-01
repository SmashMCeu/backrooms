package gg.maga.backrooms.command;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.prototype.Prototype;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
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
        player.sendMessage("Start generating backrooms...");
        Prototype prototype = new Prototype(backrooms, player.getLocation(), 23, arguments.getInteger(0));
        prototype.start();
        player.sendMessage("Finished generating backrooms");
        return true;
    }
}
