package eu.smashmc.backrooms.game.stats;

import eu.smashmc.api.SmashMc;
import eu.smashmc.api.stats.Statistics;
import eu.smashmc.api.stats.StatsHelper;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GameStatsAdapter implements GameStats {

    private StatsHelper helper;

    public GameStatsAdapter() {
        this.helper = SmashMc.getComponent(Statistics.class).createHelper("backrooms");
    }

    @Override
    public void startGame(String map, Collection<GameParticipant> participants) {
        List<Player> players = participants.stream().filter(participant -> participant instanceof ScientistParticipant ||
                participant instanceof EntityParticipant).map(GameParticipant::getPlayer).toList();
        helper.startGame(map, players);
    }

    @Override
    public void endGame() {
        helper.endGame(true);
    }

    @Override
    public void addKill(Player player) {
        helper.addKill(player);
    }

    @Override
    public void addDeath(Player player) {
        helper.addDeath(player);
    }

    @Override
    public void addRevive(Player player) {
        helper.plusIntValue("revives", player, 1);
    }

    @Override
    public void addEntityWin(Player player) {
        helper.addWin(player);
        helper.plusIntValue("entityWins", player, 1);
    }

    @Override
    public void addScientistWin(Player player) {
        helper.addWin(player);
        helper.plusIntValue("scientistWins", player, 1);
    }
}
