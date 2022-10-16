package gg.maga.backrooms.game.scoreboard;

import gg.maga.backrooms.Backrooms;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.scoreboard.sidebar.Sidebar;
import gg.maga.backrooms.game.scoreboard.tablist.Tablist;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

@Getter
@Setter
public class CustomBoard {

    private final CustomBoardRegistry registry;
    private final Backrooms backrooms;

    private final Game game;

    private Player player;
    private Scoreboard board;

    private Sidebar sidebar;
    private Tablist tablist;

    public CustomBoard(CustomBoardRegistry registry, Game game, Player player) {
        this.registry = registry;
        this.game = game;
        this.backrooms = registry.getBackrooms();
        this.player = player;
        create();
    }

    private void create() {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sidebar = new Sidebar(player, this, board);
        this.tablist = new Tablist(player, this);

        player.setScoreboard(board);
    }



    public void reset() {
        this.sidebar.destroy();
        Bukkit.getScheduler().runTaskLater(backrooms, () -> {
            create();
        }, 20);

    }

}
