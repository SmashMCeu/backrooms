package eu.smashmc.backrooms.command;

import eu.smashmc.backrooms.util.raytrace.Raytrace;
import eu.smashmc.backrooms.util.raytrace.ScreenEntityFinder;
import eu.smashmc.backrooms.util.raytrace.result.RaytraceHit;
import eu.smashmc.backrooms.util.raytrace.result.RaytraceResult;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class TestCommand extends PlayerCommand {

    public TestCommand() {
        super("test");
        setAliases("test2");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        ScreenEntityFinder finder = new ScreenEntityFinder(player);
        List<Entity> entities = finder.findEntities(50);
        player.sendMessage("Found: " + entities.size());
        for(Entity entity : entities) {
            player.sendMessage(entity.toString());
        }
        return true;
    }
}
