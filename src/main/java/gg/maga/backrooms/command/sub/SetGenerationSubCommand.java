package gg.maga.backrooms.command.sub;

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
public class SetGenerationSubCommand extends HelpSubCommand<Player> {

    private GameProvider provider;

    public SetGenerationSubCommand(GameProvider provider) {
        super("setgeneration");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "setgeneration");
        setDescription("Set the generation start point");
        this.provider = provider;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(BackroomsConstants.PREFIX + "§7You have successfully set the §ageneration start point");
        provider.getConfig().setGenerationLocation(player.getLocation());
        return true;
    }
}
