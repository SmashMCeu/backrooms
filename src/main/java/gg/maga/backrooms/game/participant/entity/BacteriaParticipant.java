package gg.maga.backrooms.game.participant.entity;

import gg.maga.backrooms.game.GameProvider;
import gg.maga.backrooms.game.GameService;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.util.Progress;
import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BacteriaParticipant extends EntityParticipant {

    private static final float WALK_SPEED = 0.25f;
    private static final float ATTACK_WALK_SPEED = 0.10f;
    private static final int ATTACK_PROGRESS_MAX_COUNT = 10;
    private static final double DAMAGE = 10;

    private int attackProgressCount;

    public BacteriaParticipant(Player player) {
        super(player, "Bacteria");
        player.setWalkSpeed(WALK_SPEED);
        DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.ENDERMAN));
    }

    @Override
    public void onAttackTarget(GameProvider provider, GameService service, Game game, ScientistParticipant target, EntityDamageByEntityEvent event) {
        if(attackProgressCount <= 0) {
            getPlayer().setWalkSpeed(ATTACK_WALK_SPEED);
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 0.6f, 1f);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 0));
            startProgress(provider.getBackrooms(), game);
            double nextHealth = target.getPlayer().getHealth() - DAMAGE;
            if(nextHealth <= 0) {
                event.setDamage(0);
                service.knock(game, target);
                return;
            }
            target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.ENTITY_BAT_HURT, 0.8f, 1);
            event.setDamage(DAMAGE);
            return;
        }
        event.setCancelled(true);
        getPlayer().sendMessage(BackroomsConstants.PREFIX + "§cYour attack is on delay");
     }

    private void startProgress(Plugin plugin, Game game) {
        this.attackProgressCount = ATTACK_PROGRESS_MAX_COUNT;
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Progress.getProgress(ATTACK_PROGRESS_MAX_COUNT, attackProgressCount, false)));
                if(attackProgressCount <= 0 || game.getState() != GameState.IN_GAME) {
                    cancel();
                    getPlayer().setWalkSpeed(WALK_SPEED);
                    return;
                }
                attackProgressCount--;
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
