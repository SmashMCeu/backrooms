package eu.smashmc.backrooms.command;

import eu.smashmc.backrooms.command.sub.*;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.Backrooms;
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
    private GameService service;

    public BackroomsCommand(Backrooms backrooms) {
        super("backrooms", "Backrooms");
        setSenders(Player.class);
        setAliases("br", "backroom");
        setBaseColor("§e");
        this.backrooms = backrooms;

        addChild(new GenerateSubCommand(backrooms));
        addChild(new ScanSubCommand(backrooms));
        addChild(new ClearSubCommand(backrooms));
        addChild(new JoinSubCommand(backrooms));
        addChild(new LeaveSubCommand(backrooms));
        addChild(new SetLobbySubCommand(backrooms));
        addChild(new SetGenerationStartSubCommand(backrooms));
        addChild(new StartSubCommand(backrooms));
        addChild(new EntitySubCommand(backrooms));
    }


}
