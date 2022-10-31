package eu.smashmc.backrooms.command.sub;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.GameService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LeaveSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public LeaveSubCommand(Backrooms backrooms) {
        super("leave");
        setDescription("Leave your current game");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "leave");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        GameService service = backrooms.getGameProvider().getService();
        Optional<Game> optional = service.getGameByPlayer(player);
        if(optional.isPresent()) {
            service.leaveGame(optional.get(), player, false);
        } else {
            player.sendMessage(BackroomsConstants.PREFIX + "§cYou are not in a game.");
        }
        return true;
    }
}
