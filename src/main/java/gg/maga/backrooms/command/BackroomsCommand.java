package gg.maga.backrooms.command;

import com.google.common.base.Joiner;
import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.room.Room;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class BackroomsCommand extends PlayerCommand {

    private final Backrooms backrooms;

    @Inject
    private GameProvider provider;

    public BackroomsCommand(Backrooms backrooms) {
        super("backrooms");
        setAliases("br", "backroom");

        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 1) {
            final String sub = arguments.getString(0);
            if(sub.equalsIgnoreCase("generate")) {
                player.sendMessage(BackroomsConstants.PREFIX + "§7Start generating backrooms...");
                provider.prepareGame().thenAccept(game -> {
                    player.sendMessage(BackroomsConstants.PREFIX + "§7ID: §a" + game.getId());
                    player.sendMessage(BackroomsConstants.PREFIX + "§7Scientist spawns: §a" + game.getMap().getScientistSpawns().size());
                    player.sendMessage(BackroomsConstants.PREFIX + "§7Entity spawns: §a" + game.getMap().getEntitySpawns().size());

                    player.sendMessage(BackroomsConstants.PREFIX + "§aSuccessfully finished generating backrooms");

                });
                return true;
            } else if(sub.equalsIgnoreCase("clear")) {
                player.sendMessage(BackroomsConstants.PREFIX + "§7Clearing all backrooms.");
                provider.clearGames().thenAccept(unused -> {
                    player.sendMessage(BackroomsConstants.PREFIX + "§aSuccessfully cleared all backrooms");
                });
            }

            return true;
        }
        player.sendMessage(BackroomsConstants.PREFIX + "§cUsage: /backrooms <generate, clear>");
        return true;
    }

    private void printRoom(Player player, Room first) {
        player.sendMessage("Min: " + first.getMin().getBlockX() + " / " + first.getMin().getBlockY() + " / " + first.getMin().getBlockZ());
        player.sendMessage("Max: " + first.getMax().getBlockX() + " / " + first.getMax().getBlockY() + " / " + first.getMax().getBlockZ());
        player.sendMessage("Openings: " + Joiner.on(", ").join(first.getOpenings()));
    }
}
