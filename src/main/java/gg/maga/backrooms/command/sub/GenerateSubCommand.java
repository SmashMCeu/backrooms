package gg.maga.backrooms.command.sub;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.GameProvider;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.NoPermissionException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotSubCommand;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GenerateSubCommand extends HelpSubCommand<Player> {

    private GameProvider provider;

    public GenerateSubCommand(GameProvider provider) {
        super("generate");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "generate");
        setDescription("Generate a backroom");
        this.provider = provider;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(BackroomsConstants.PREFIX + "§7Start generating backrooms...");
        provider.prepareGame().thenAccept(game -> {
            player.sendMessage(BackroomsConstants.PREFIX + "§7ID: §a" + game.getId());
            player.sendMessage(BackroomsConstants.PREFIX + "§7Scientist spawns: §a" + game.getMap().getScientistSpawns().size());
            player.sendMessage(BackroomsConstants.PREFIX + "§7Entity spawns: §a" + game.getMap().getEntitySpawns().size());

            player.sendMessage(BackroomsConstants.PREFIX + "§aSuccessfully finished generating backrooms");

        });
        return true;
    }
}
