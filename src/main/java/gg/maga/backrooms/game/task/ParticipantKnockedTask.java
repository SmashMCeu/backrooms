package gg.maga.backrooms.game.task;

import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.util.Progress;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistState;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.spigot.hologram.Hologram;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class ParticipantKnockedTask extends BukkitRunnable {

    private static final int MAX_REVIVING_COUNT = 10;

    private final Game game;
    private final ScientistParticipant participant;

    private int revivingCount;

    @Override
    public void run() {
        if(game.getState() != GameState.IN_GAME || participant.getState() != ScientistState.KNOCKED) {
            if (participant.getKnockedHologram() != null) {
                participant.getKnockedHologram().disable();
            }
            cancel();
            return;
        }
        Player player = participant.getPlayer();
        if(player.getLocation().distanceSquared(participant.getKnockedLocation()) >= 10) {
            player.teleport(participant.getKnockedLocation());
        }
        long time = (System.currentTimeMillis() - participant.getKnockedTimestamp()) * 1000;
        String[] lines = new String[] {
                "§e§l" + player.getName(),
                "§7Time: §c" + TimeUtil.convertToTwoDigits(time),
                "§7Hold §eshift §7to revive"
        };
        if(participant.getKnockedHologram() == null) {
            Hologram hologram = new Hologram(participant.getKnockedLocation(),
                    lines);
            hologram.enable();
        } else {
            Hologram hologram = participant.getKnockedHologram();
            hologram.updateLines(lines);
        }

        for(GameParticipant otherParticipants : game.getParticipantRegistry().getParticipants().values()) {
            if(otherParticipants instanceof ScientistParticipant scientist) {
                if(scientist.getState() == ScientistState.ALIVE && scientist.getPlayer().isSneaking()) {
                    if(scientist.getPlayer().getLocation().distanceSquared(player.getLocation()) <= 4) {
                        if(revivingCount >= MAX_REVIVING_COUNT) {
                            game.getProvider().getMatchmaker().revive(game, scientist, participant);
                            return;
                        }
                        final String actionBarMessage = Progress.getProgress(MAX_REVIVING_COUNT, revivingCount, false);
                        final BaseComponent[] component = TextComponent.fromLegacyText(actionBarMessage);
                        scientist.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                        participant.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                        revivingCount++;
                        return;
                    }
                }
            }
        }
        revivingCount = 0;
    }
}
