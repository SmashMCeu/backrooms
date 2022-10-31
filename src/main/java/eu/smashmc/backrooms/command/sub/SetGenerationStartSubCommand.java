package eu.smashmc.backrooms.command.sub;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameProvider;
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
public class SetGenerationStartSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public SetGenerationStartSubCommand(Backrooms backrooms) {
        super("setgenerationstart");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "setgenerationstart");
        setDescription("Set the generation start point");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        GameProvider provider = backrooms.getGameProvider();
        player.sendMessage(BackroomsConstants.PREFIX + "§7You have successfully set the §ageneration start point");
        provider.getConfigProvider().getEntity().getGame().setGenerationStart(player.getLocation());
        provider.getConfigProvider().save();
        return true;
    }
}
