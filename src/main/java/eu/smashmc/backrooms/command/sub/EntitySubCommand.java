package eu.smashmc.backrooms.command.sub;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class EntitySubCommand extends HelpSubCommand<Player> {

    public static UUID ENFORCED_ENTITY = null;

    private Backrooms backrooms;

    public EntitySubCommand(Backrooms backrooms) {
        super("entity");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "entity");
        setDescription("Enforce yourself as entity");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(BackroomsConstants.PREFIX + "§7You enforced yourself as a §aentity");
        ENFORCED_ENTITY = player.getUniqueId();
        return true;
    }
}
