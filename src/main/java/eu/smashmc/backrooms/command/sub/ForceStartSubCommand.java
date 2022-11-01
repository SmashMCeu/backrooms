package eu.smashmc.backrooms.command.sub;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ForceStartSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public ForceStartSubCommand(Backrooms backrooms) {
        super("forcestart");
        setDescription("Forcefully start the game");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "forcestart");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        GameService service = backrooms.getGameProvider().getService();
        Optional<Game> optional = service.getGameByPlayer(player);
        if(optional.isPresent()) {
            Game game = optional.get();
            if(game.getState() == GameState.LOBBY) {
                game.getProperties().setMaxPlayers(Bukkit.getOnlinePlayers().size());
                game.getCountdown().setCurrentCount(0);
                if(!game.getCountdown().isRunning()) {
                    game.getCountdown().start();
                }
                player.sendMessage(BackroomsConstants.PREFIX + "§7Successfully force started the §agame");
            } else {
                player.sendMessage(BackroomsConstants.PREFIX + "§cThis game is already running.");
            }
        } else {
            player.sendMessage(BackroomsConstants.PREFIX + "§cYou are not in a game.");
        }
        return true;
    }
}
