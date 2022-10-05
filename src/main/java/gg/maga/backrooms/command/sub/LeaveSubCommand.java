package gg.maga.backrooms.command.sub;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.GameMatchmaker;
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
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        GameMatchmaker matchmaker = backrooms.getGameProvider().getMatchmaker();
        Optional<Game> optional = matchmaker.getGameByPlayer(player);
        if(optional.isPresent()) {
            matchmaker.leaveGame(optional.get(), player);
        } else {
            player.sendMessage(BackroomsConstants.PREFIX + "§cYou are not in a game.");
        }
        return true;
    }
}
