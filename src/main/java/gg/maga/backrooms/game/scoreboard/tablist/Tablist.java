package gg.maga.backrooms.game.scoreboard.tablist;

import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.scoreboard.CustomBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
    private List<Team> teams;


    public Tablist(Player player, CustomBoard board) {
        this.player = player;
        this.board = board;

        this.teams = new ArrayList<>();

        create();
        update();
    }

    private void create() {
        createTeam("AEntity", ChatColor.RED);
        createTeam("BScientist", ChatColor.YELLOW);
        createTeam("CLobby", ChatColor.GRAY);
    }

    private Team createTeam(String name, ChatColor color) {
        if(board.getBoard().getTeam(name) != null) {
            return null;
        }
        Team team = board.getBoard().registerNewTeam(name);
        team.setColor(color);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        this.teams.add(team);
        return team;
    }

    public void update() {
        for (Team team : teams) {
            for (String entry : team.getEntries()) {
                team.removeEntry(entry);
            }
        }

        for (GameParticipant participant : board.getGame().getParticipantRegistry().getParticipants().values()) {
            Player player = participant.getPlayer();
            Team team = null;
            if (participant instanceof ScientistParticipant) {
                team = board.getBoard().getTeam("BScientist");
            } else if (participant instanceof EntityParticipant) {
                team = board.getBoard().getTeam("AEntity");
            } else {
                team = board.getBoard().getTeam("CLobby");
            }
            if (team != null) {
                if (!team.hasEntry(player.getName())) {
                    team.addEntry(player.getName());
                }
            }

        }


    }
}
