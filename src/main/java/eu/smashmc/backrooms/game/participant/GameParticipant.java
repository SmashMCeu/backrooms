package eu.smashmc.backrooms.game.participant;

import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameParticipant {

    private Player player;
    private String tabColor;

    public void disableJump() {
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, false, false), true);
    }

    public void onUpdate(GameProvider provider, GameService service, Game game) {

    }


}
