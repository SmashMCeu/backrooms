package gg.maga.backrooms.game.task;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.util.Progress;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistState;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.spigot.hologram.Hologram;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@RequiredArgsConstructor
public class ParticipantKnockedTask extends BukkitRunnable {

    private static final int MAX_REVIVING_COUNT = 10;

    private final Game game;

    private final GameService service;
    private final GameProvider provider;
    private final ScientistParticipant participant;

    private int revivingCount;

    @Override
    public void run() {
        if(game.getState() != GameState.IN_GAME || participant.getState() != ScientistState.KNOCKED ||
        !provider.existsGame(game.getId())) {
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
            participant.setKnockedHologram(hologram);
        } else {
            Hologram hologram = participant.getKnockedHologram();
            hologram.updateLines(lines);
        }

        for(GameParticipant otherParticipants : game.getParticipantRegistry().getParticipants().values()) {
            if(otherParticipants instanceof ScientistParticipant scientist ) {
                if(scientist.getPlayer().isSneaking()) {
                    if(scientist.getPlayer().getLocation().distanceSquared(player.getLocation()) <= 4) {
                        if(revivingCount >= MAX_REVIVING_COUNT) {
                            service.revive(game, scientist, participant);
                            participant.getPlayer().getWorld().playSound(participant.getKnockedLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1);
                            participant.getPlayer().sendTitle("§7You have been §arevived", "", 20, 20, 20);
                            return;
                        }
                        participant.getPlayer().getWorld().playSound(participant.getKnockedLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.6f, 1);
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
