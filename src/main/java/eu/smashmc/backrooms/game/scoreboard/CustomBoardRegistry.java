package eu.smashmc.backrooms.game.scoreboard;

import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.game.model.Game;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service
public class CustomBoardRegistry {

    private final Backrooms backrooms;
    private Map<UUID, CustomBoard> boards;

    public CustomBoardRegistry(Backrooms backrooms) {
        this.backrooms = backrooms;
        this.boards = new HashMap<>();
        Bukkit.getScheduler().runTaskTimer(backrooms, new CustomBoardUpdater(this), 20, 20);
    }

    public void updateTablistAll() {
        for(CustomBoard board : boards.values()) {
            board.getTablist().update();
        }
    }

    public void add(Player player, Game game) {
        if(boards.containsKey(player.getUniqueId())) {
            return;
        }
        CustomBoard board = new CustomBoard(this, game, player);
        boards.put(player.getUniqueId(), board);
    }

    public void remove(Player player) {
        if(!boards.containsKey(player.getUniqueId())) {
            return;
        }
        CustomBoard board = boards.remove(player.getUniqueId());
        board.getSidebar().destroy();
        board.getTablist().destroy();
        for(Team team : board.getBoard().getTeams()) {
            team.unregister();
        }
    }

    public CustomBoard getBoardByPlayer(Player player) {
        return boards.get(player.getUniqueId());
    }

    public void resetSidebar(Player player) {
        if(boards.containsKey(player.getUniqueId())) {
            CustomBoard board = boards.get(player.getUniqueId());
            board.reset();
        }
    }
}
