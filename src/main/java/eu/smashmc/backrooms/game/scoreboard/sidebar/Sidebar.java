package eu.smashmc.backrooms.game.scoreboard.sidebar;

import eu.smashmc.backrooms.game.scoreboard.CustomBoard;
import eu.smashmc.backrooms.game.scoreboard.sidebar.score.SidebarScore;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
import eu.smashmc.backrooms.game.scoreboard.sidebar.score.SidebarScoreType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
        GameParticipant gameParticipant = game.getParticipantRegistry().getParticipant(player.getUniqueId());

        if(game.getState() == GameState.IN_GAME || game.getState() == GameState.END) {
            if(gameParticipant instanceof EntityParticipant entity) {

            } else if(gameParticipant instanceof ScientistParticipant scientist) {
                addStaticLine("§3§7 ");
                addStaticLine(" §e§l" + player.getName());
                addDynamicLine("  §8➥ §7Lives§8: ", "Lives", team -> {
                    team.setSuffix("§6" + scientist.getKnocks());
                });
            }

            addStaticLine("§a§3§7 ");
            addStaticLine(" §e§lGame");
            addDynamicLine("  §8➥ §7Tasks§8: ", "Tasks", team -> {
                team.setSuffix("§a" + game.getSolvedTasks() + "§8/§a" + game.getProperties().getMaxTasks());
            });
            addDynamicLine("  §8➥ §7Scientists§8: ", "Scientists", team -> {
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
            addDynamicLine("  §8➥ §7Entities§8: ", "Entities", team -> {
                int count = 0;
                for(GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
                    if(participant instanceof EntityParticipant) {
                        count++;
                    }
                }
                team.setSuffix("§c" + count + "§8/§c" + game.getProperties().getMaxEntities());
            });
            addStaticLine("§3§7§1 ");
        } else if(game.getState() == GameState.LOBBY) {
            addStaticLine("§3§7 ");
            addStaticLine(" §e§lPlayers");
            addDynamicLine("  §8➥ ", "Players", team -> {
                team.setSuffix("§7" + Bukkit.getOnlinePlayers().size() + "§8/§7" + Bukkit.getMaxPlayers());
            });
            addStaticLine("§3§7§8§6 ");
            addStaticLine(" §e§lMap");
            addDynamicLine("  §8➥ ", "Map", team -> {
                team.setSuffix("§7Level 0");
            });
            addStaticLine("§3§8§7                   §3§8§7");
        }


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
        sidebar.setDisplayName("§e§lBackrooms");

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
        for (SidebarScore score : scores) {
            if(score.getScoreName() != null) {
                Team team = board.getTeam(score.getScoreName());
                if(team != null) {
                    team.unregister();
                }
            }

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
