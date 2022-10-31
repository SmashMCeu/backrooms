package eu.smashmc.backrooms.game.scoreboard.tablist;

import eu.smashmc.api.Constants;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.scoreboard.CustomBoard;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.lib.bukkit.tablist.TablistEntry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Tablist {

    private CustomBoard board;
    private Player player;

    private eu.smashmc.lib.bukkit.tablist.Tablist tablist;

    public Tablist(Player player, CustomBoard board) {
        this.player = player;
        this.board = board;
        this.tablist = board.getBackrooms().getSmashLib().getTablist();

        update();
    }

    public void destroy() {

    }


    public void update() {
        Game game = board.getGame();
        GameParticipant participant = game.getParticipantRegistry().getParticipant(player.getUniqueId());
        TablistEntry entry = tablist.getEntry(player);
        if (game.getState() != GameState.LOBBY) {
            entry.setPrefix(participant.getTabColor());
            player.setPlayerListName(participant.getTabColor() + participant.getPlayer().getName());
            entry.setNametagVisibility(NameTagVisibility.NEVER);
        } else {
            entry.update();
        }

    }
}
