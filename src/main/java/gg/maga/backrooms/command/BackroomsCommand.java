package gg.maga.backrooms.command;

import com.google.common.base.Joiner;
import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.command.sub.ClearSubCommand;
import gg.maga.backrooms.command.sub.GenerateSubCommand;
import gg.maga.backrooms.command.sub.JoinSubCommand;
import gg.maga.backrooms.command.sub.LeaveSubCommand;
import gg.maga.backrooms.game.Game;
import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.room.Room;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.NoPermissionException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class BackroomsCommand extends HelpCommand<Player> {

    private final Backrooms backrooms;

    @Inject
    private GameProvider provider;

    @Inject
    private GameMatchmaker matchmaker;

    public BackroomsCommand(Backrooms backrooms) {
        super("backrooms", "Backrooms");
        setSenders(Player.class);
        setAliases("br", "backroom");
        this.backrooms = backrooms;

        addChild(new GenerateSubCommand(provider));
        addChild(new ClearSubCommand(provider));
        addChild(new JoinSubCommand(matchmaker));
        addChild(new LeaveSubCommand(matchmaker));
    }


}
