package eu.smashmc.backrooms.command.sub;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.item.BackroomItemRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ItemSubCommand extends HelpSubCommand<Player> {

    private Backrooms backrooms;

    public ItemSubCommand(Backrooms backrooms) {
        super("item");
        setPermission(BackroomsConstants.PERMISSION_PREFIX + "item");
        setDescription("Get an item");
        setAliases("i", "items");
        this.backrooms = backrooms;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        BackroomItemRegistry registry = backrooms.getGameProvider().getService().getItemRegistry();
        if(arguments.getLength() >= 2) {
            String id = "";
            for (int i = 1; i < arguments.getArgs().length; i++) {
                id += arguments.getArgs()[i] + " ";
            }
            id = id.trim();
            if(!registry.existsItemById(id)) {
                player.sendMessage(BackroomsConstants.PREFIX + "§cThis item does not exists.");
                return true;
            }
            player.sendMessage(BackroomsConstants.PREFIX + "§7You received the item §a" + id);
            player.getInventory().addItem(registry.createItem(id));
            return true;
        }
        player.sendMessage(" ");
        player.sendMessage(BackroomsConstants.PREFIX + "§7Items§8:");
        for(String item : registry.getItems().keySet()) {
            player.sendMessage( BackroomsConstants.PREFIX + "  §8- §a" + item);
        }
        player.sendMessage(" ");
        return true;
    }

}
