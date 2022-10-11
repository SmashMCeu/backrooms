package gg.maga.backrooms.game.scoreboard.sidebar;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistState;
import gg.maga.backrooms.game.scoreboard.CustomBoard;
import gg.maga.backrooms.game.scoreboard.sidebar.score.SidebarScore;
import gg.maga.backrooms.game.scoreboard.sidebar.score.SidebarScoreType;
import in.prismar.library.common.math.NumberFormatter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class Sidebar {

    private Player player;

    private CustomBoard customBoard;
    private Scoreboard board;
    private List<SidebarScore> scores;

    public Sidebar(Player player, CustomBoard customBoard, Scoreboard board) {
        this.player = player;
        this.customBoard = customBoard;
        this.board = board;
        this.scores = new ArrayList<>();

        create();

    }

    private void createGameTeams() {
        this.scores.clear();

        Game game = customBoard.getGame();

        addStaticLine("§3§7 ");
        addStaticLine("§8┃ §e" + player.getName());
        addDynamicLine(" §8▪ §7Knocks§8: ", "Knocks", team -> {
            team.setSuffix("§62");
        });
        addStaticLine("§a§3§7 ");
        addStaticLine("§8┃ §eGame");
        addDynamicLine(" §8▪ §7Tasks§8: ", "Tasks", team -> {
            team.setSuffix("§a" + game.getSolvedTasks() + "§8/§a" + game.getProperties().getMaxTasks());
        });
        addDynamicLine(" §8▪ §7Scientists§8: ", "Scientists", team -> {
            int count = 0;
            for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
                if(participant instanceof ScientistParticipant scientistParticipant) {
                    if(scientistParticipant.getState() == ScientistState.ALIVE) {
                        count++;
                    }
                }
            }
            team.setSuffix("§e" + count + "§8/§e" + game.getProperties().getMaxScientists());
        });
        addDynamicLine(" §8▪ §7Entities§8: ", "Entities", team -> {
            int count = 0;
            for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
                if(participant instanceof EntityParticipant) {
                    count++;
                }
            }
            team.setSuffix("§c" + count + "§8/§c" + game.getProperties().getMaxEntities());
        });
        addStaticLine("§3§7§1 ");
    }

    public void addStaticLine(String content) {
        this.scores.add(new SidebarScore(SidebarScoreType.STATIC, content));
    }

    public void addDynamicLine(String content, String name, Consumer<Team> update) {
        this.scores.add(new SidebarScore(SidebarScoreType.DYNAMIC, content, name, update));
    }


    private void createObjective() {
        if (board.getObjective(DisplaySlot.SIDEBAR) != null) {
            board.getObjective(DisplaySlot.SIDEBAR).unregister();
        }


        Objective sidebar = board.registerNewObjective("Sidebar", "dummy", "DisplayName");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName("§eBackrooms");

        int index = scores.size();
        for (SidebarScore score : scores) {
            if (score.getType() == SidebarScoreType.DYNAMIC) {
                Team team = board.registerNewTeam(score.getScoreName());
                team.addEntry(score.getContent());
            }
            sidebar.getScore(score.getContent()).setScore(index);
            index--;
        }
        update();
    }

    public void create() {
        createGameTeams();
        createObjective();
    }

    public void destroy() {
        for (Team team : board.getTeams()) {
            team.unregister();
        }
        if (board.getObjective(DisplaySlot.SIDEBAR) != null) {
            board.getObjective(DisplaySlot.SIDEBAR).unregister();
        }
    }

    public void update() {
        for (SidebarScore score : scores) {
            if (score.getType() == SidebarScoreType.DYNAMIC) {
                Team team = board.getTeam(score.getScoreName());
                if (team != null) {
                    score.getUpdate().accept(team);
                }
            }
        }
    }
}
