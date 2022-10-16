package gg.maga.backrooms.game.participant.entity;

import gg.maga.backrooms.util.Progress;
import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BacteriaParticipant extends EntityParticipant {

    private static final float WALK_SPEED = 0.25f;
    private static final int ATTACK_PROGRESS_MAX_COUNT = 10;
    private static final double DAMAGE = 10;

    private int attackProgressCount;

    public BacteriaParticipant(Player player) {
        super(player, "Bacteria");
        player.setWalkSpeed(WALK_SPEED);
        DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.ENDERMAN));
    }

    @Override
    public void onAttackTarget(Game game, ScientistParticipant target, EntityDamageByEntityEvent event) {
        if(attackProgressCount <= 0) {
            startProgress(game);
            double nextHealth = target.getPlayer().getHealth() - DAMAGE;
            if(nextHealth <= 0) {
                event.setDamage(0);
                game.getProvider().getMatchmaker().knock(game, target);
                return;
            }
            event.setDamage(DAMAGE);
            return;
        }
        event.setCancelled(true);
        getPlayer().sendMessage(BackroomsConstants.PREFIX + "§cYour attack is on delay");
     }

    private void startProgress(Game game) {
        this.attackProgressCount = ATTACK_PROGRESS_MAX_COUNT;
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Progress.getProgress(ATTACK_PROGRESS_MAX_COUNT, attackProgressCount, false)));
                if(attackProgressCount <= 0) {
                    cancel();
                    return;
                }
                attackProgressCount--;
            }
        }.runTaskTimer(game.getProvider().getBackrooms(), 20, 20);
    }
}
