package gg.maga.backrooms.command.sub;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.GameProvider;
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
public class ClearSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public ClearSubCommand(Backrooms backrooms) {
        super("clear");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "clear");
        setDescription("Clear all backrooms");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(BackroomsConstants.PREFIX + "§7Clearing all backrooms.");
        backrooms.getGameProvider().clearGames().thenAccept(unused -> {
            player.sendMessage(BackroomsConstants.PREFIX + "§aSuccessfully cleared all backrooms");
        });
        return true;
    }
}
