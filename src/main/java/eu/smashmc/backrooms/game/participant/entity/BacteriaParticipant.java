package eu.smashmc.backrooms.game.participant.entity;

import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
import eu.smashmc.backrooms.util.Progress;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
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
    private static final float NORMAL_WALK_SPEED = 0.23f;
    private static final float AGRO_WALK_SPEED = 0.3f;
    private static final float STUN_WALK_SPEED = 0.13f;
    private static final int AGRO_TIMEOUT = 10;
    private static final int STUN_DURATION = 20 * 10;
    private static final int ATTACK_PROGRESS_MAX_COUNT = 8;
    private static final double DAMAGE = 10;
    private static final double LAST_SOUND_DISTANCE_SECONDS = 12;

    private static final String BACTERIA_SOUND = "custom:bacteria";


    private int attackProgressCount;

    private long lastSoundTimestamp;
    private long lastSeenScientist;

    private boolean stunned;

    public BacteriaParticipant(Player player) {
        super(player, "Bacteria");
        DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.ENDERMAN));
    }

    @Override
    public void onUpdate(GameProvider provider, GameService service, Game game) {
        getPlayer().setFoodLevel(2);
        long difference = (System.currentTimeMillis() - lastSeenScientist) / 1000;
        if(stunned) {
            getPlayer().setWalkSpeed(STUN_WALK_SPEED);
            return;
        }
        if(difference <= AGRO_TIMEOUT) {
            getPlayer().setWalkSpeed(AGRO_WALK_SPEED);
        } else {
            getPlayer().setWalkSpeed(NORMAL_WALK_SPEED);
        }
    }

    @Override
    public void stun(GameProvider provider, GameService service, Game game, boolean blindness) {
        stunned = true;
        if(blindness) {
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, STUN_DURATION, 1, false, false), true);
        }
        Bukkit.getScheduler().runTaskLater(provider.getBackrooms(), () -> {
            stunned = false;
        }, STUN_DURATION);
    }

    @Override
    public void onSeeScientist(GameProvider provider, GameService service, Game game, ScientistParticipant scientist) {
        if(scientist.getState() != ScientistState.ALIVE || stunned) {
            return;
        }
        long distance = (System.currentTimeMillis() - lastSoundTimestamp) / 1000;
        if(distance >= LAST_SOUND_DISTANCE_SECONDS) {
            lastSoundTimestamp = System.currentTimeMillis();
            getPlayer().getWorld().playSound(getPlayer().getLocation(), BACTERIA_SOUND, 0.4f, 1f);
        }
        lastSeenScientist = System.currentTimeMillis();


    }

    @Override
    public void onAttackTarget(GameProvider provider, GameService service, Game game, ScientistParticipant target, EntityDamageByEntityEvent event) {
        if(attackProgressCount <= 0) {
            stun(provider, service, game, false);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, STUN_DURATION / 2, 200, false, false), true);
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
