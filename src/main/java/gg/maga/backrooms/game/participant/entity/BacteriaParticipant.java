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
import org.bukkit.Bukkit;
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

    private static final float NORMAL_WALK_SPEED = 0.15f;
    private static final float AGRO_WALK_SPEED = 0.25f;
    private static final float STUN_WALK_SPEED = 0.10f;
    private static final int AGRO_TIMEOUT = 10;
    private static final int STUN_DURATION = 20 * 10;
    private static final int ATTACK_PROGRESS_MAX_COUNT = 10;
    private static final double DAMAGE = 10;
    private static final double LAST_SOUND_DISTANCE_SECONDS = 60;

    private static final String BACTERIA_SOUND = "custom:bacteria";


    private int attackProgressCount;

    private long lastSoundTimestamp;
    private long lastSeenScientist;

    private boolean stunned;

    public BacteriaParticipant(Player player) {
        super(player, "Bacteria");
        player.setWalkSpeed(NORMAL_WALK_SPEED);
        DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.ENDERMAN));
    }

    @Override
    public void onUpdate(GameProvider provider, GameService service, Game game) {
        long difference = (System.currentTimeMillis() - lastSeenScientist) / 1000;
        if(difference <= AGRO_TIMEOUT) {
            if(!stunned) {
                getPlayer().setWalkSpeed(AGRO_WALK_SPEED);
            }
        } else {
            if(!stunned) {
                getPlayer().setWalkSpeed(NORMAL_WALK_SPEED);
            }
        }
    }

    @Override
    public void stun(GameProvider provider, GameService service, Game game, boolean blindness) {
        stunned = true;
        if(blindness) {
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, STUN_DURATION, 1));
        }
        getPlayer().setWalkSpeed(STUN_WALK_SPEED);
        Bukkit.getScheduler().runTaskLater(provider.getBackrooms(), () -> {
            getPlayer().setWalkSpeed(NORMAL_WALK_SPEED);
            stunned = false;
        }, STUN_DURATION);
    }

    @Override
    public void onSeeScientist(GameProvider provider, GameService service, Game game, ScientistParticipant scientist) {
        long distance = (System.currentTimeMillis() - lastSoundTimestamp) / 1000;
        if(distance >= LAST_SOUND_DISTANCE_SECONDS) {
            lastSoundTimestamp = System.currentTimeMillis();
            getPlayer().getWorld().playSound(getPlayer().getLocation(), BACTERIA_SOUND, 0.5f, 1f);
        }
        lastSeenScientist = System.currentTimeMillis();


    }

    @Override
    public void onAttackTarget(GameProvider provider, GameService service, Game game, ScientistParticipant target, EntityDamageByEntityEvent event) {
        if(attackProgressCount <= 0) {
            stun(provider, service, game, false);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, STUN_DURATION / 2, 200));
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 0.6f, 1f);
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
                    return;
                }
                attackProgressCount--;
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
