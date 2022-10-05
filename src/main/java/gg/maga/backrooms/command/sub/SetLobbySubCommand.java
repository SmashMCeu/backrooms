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
public class SetLobbySubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public SetLobbySubCommand(Backrooms backrooms) {
        super("setlobby");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "setlobby");
        setDescription("Set the lobby spawn");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        GameProvider provider = backrooms.getGameProvider();
        player.sendMessage(BackroomsConstants.PREFIX + "§7You have successfully set the §alobby spawn");
        provider.getConfigProvider().getEntity().getGame().setLobby(player.getLocation());
        provider.getConfigProvider().save();
        return true;
    }
}
