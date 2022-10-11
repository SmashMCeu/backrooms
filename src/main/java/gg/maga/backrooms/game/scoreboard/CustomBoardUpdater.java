package gg.maga.backrooms.game.scoreboard;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomBoardUpdater implements Runnable {

    private final CustomBoardRegistry registry;

    @Override
    public void run() {
        for(CustomBoard board : registry.getBoards().values()) {
            board.getSidebar().update();
        }

    }
}
