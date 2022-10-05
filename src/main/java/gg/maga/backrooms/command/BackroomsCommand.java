package gg.maga.backrooms.command;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.command.sub.*;
import gg.maga.backrooms.game.GameMatchmaker;
import gg.maga.backrooms.game.GameProvider;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

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
        addChild(new SetLobbySubCommand(provider));
        addChild(new SetGenerationSubCommand(provider));
    }


}
