package eu.smashmc.backrooms.game.scoreboard.sidebar.score;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.scoreboard.Team;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class SidebarScore {


    private final SidebarScoreType type;
    private final String content;

    private String scoreName;
    private Consumer<Team> update;
}
