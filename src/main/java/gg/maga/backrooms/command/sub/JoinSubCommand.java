package gg.maga.backrooms.command.sub;

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
public class JoinSubCommand extends HelpSubCommand<Player> {

    private GameMatchmaker matchmaker;

    public JoinSubCommand(GameMatchmaker matchmaker) {
        super("join");
        setDescription("Join a game");
        this.matchmaker = matchmaker;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        Optional<Game> optional = matchmaker.findGame();
        if(optional.isPresent()) {
            Game game = optional.get();
            matchmaker.joinGame(game, player);
        } else {
            player.sendMessage(BackroomsConstants.PREFIX + "§cNo game found");
        }
        return true;
    }
}
