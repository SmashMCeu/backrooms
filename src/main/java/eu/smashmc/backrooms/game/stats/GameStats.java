package eu.smashmc.backrooms.game.stats;

import eu.smashmc.backrooms.game.participant.GameParticipant;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface GameStats {

    void startGame(String map, Collection<GameParticipant> participants);
    void endGame();
    void addKill(Player player);

    void addRevive(Player player);
    void addDeath(Player player);

    void addEntityWin(Player player);
    void addScientistWin(Player player);


}
