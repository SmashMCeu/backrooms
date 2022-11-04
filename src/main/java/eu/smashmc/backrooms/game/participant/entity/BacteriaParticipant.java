package eu.smashmc.backrooms.game.participant.entity;

import eu.smashmc.backrooms.config.model.participant.ParticipantConfig;
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
import org.bukkit.entity.Entity;
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



    private int attackDelayCount;

    private long lastSoundTimestamp;
    private long lastSeenScientist;

    private long lastStunned;


    public BacteriaParticipant(Player player) {
        super(player, "Bacteria");
        DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.ENDERMAN));
    }

    @Override
    public void onUpdate(GameProvider provider, GameService service, Game game) {
        ParticipantConfig config = provider.getConfigProvider().getEntity().getParticipant();

        getPlayer().setFoodLevel(2);
        long difference = (System.currentTimeMillis() - lastSeenScientist) / 1000;
        if(isStunned(config)) {
            getPlayer().setWalkSpeed(config.getBacteria().getStunWalkSpeed());
            return;
        }
        if(difference <= config.getBacteria().getAgroDuration()) {
            getPlayer().setWalkSpeed(config.getBacteria().getAgroWalkSpeed());
        } else {
            getPlayer().setWalkSpeed(config.getBacteria().getNormalWalkSpeed());
        }
    }

    public boolean isStunned(ParticipantConfig config) {
        long difference = System.currentTimeMillis() - lastStunned;
        return difference <= 1000 * config.getBacteria().getStunDuration();
    }

    @Override
    public void stun(GameProvider provider, GameService service, Game game, boolean blindness) {
        lastStunned = System.currentTimeMillis();
        if(blindness) {
            ParticipantConfig config = provider.getConfigProvider().getEntity().getParticipant();
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, config.getBacteria().getStunDuration() * 20, 1, false, false));
        }
    }

    @Override
    public void onSeeScientist(GameProvider provider, GameService service, Game game, ScientistParticipant scientist) {
        ParticipantConfig config = provider.getConfigProvider().getEntity().getParticipant();
        if(scientist.getState() != ScientistState.ALIVE || isStunned(config)) {
            return;
        }
        long distance = (System.currentTimeMillis() - lastSoundTimestamp) / 1000;
        if(distance >= config.getBacteria().getAgroSoundDelay()) {
            lastSoundTimestamp = System.currentTimeMillis();
            double soundDistance = config.getBacteria().getAgroSoundBlockDistance();
            for(Entity entity : getPlayer().getWorld().getNearbyEntities(getPlayer().getLocation(), soundDistance,
                    soundDistance, soundDistance)) {
                if(entity instanceof Player target) {
                    target.playSound(getPlayer().getLocation(), config.getBacteria().getAgroSound(), 0.5f, 1f);
                }
            }

        }
        lastSeenScientist = System.currentTimeMillis();
    }

    @Override
    public void onAttackTarget(GameProvider provider, GameService service, Game game, ScientistParticipant target, EntityDamageByEntityEvent event) {
        ParticipantConfig config = provider.getConfigProvider().getEntity().getParticipant();
        if(attackDelayCount <= 0) {
            stun(provider, service, game, false);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, config.getBacteria().getStunDuration() / 2, 200, false, false));
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 0.6f, 1f);
            startAttackProgress(provider.getBackrooms(), provider, game);
            double nextHealth = target.getPlayer().getHealth() - config.getBacteria().getDamage();
            if(nextHealth <= 0) {
                event.setDamage(0);
                service.knock(game, target);
                return;
            }
            target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.ENTITY_BAT_HURT, 0.8f, 1);
            event.setDamage(config.getBacteria().getDamage());
            return;
        }
        event.setCancelled(true);
        getPlayer().sendMessage(BackroomsConstants.PREFIX + "§cYour attack is on delay");
     }

    private void startAttackProgress(Plugin plugin, GameProvider provider, Game game) {
        ParticipantConfig config = provider.getConfigProvider().getEntity().getParticipant();
        this.attackDelayCount = config.getBacteria().getAttackDelay();
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Progress.getProgress(config.getBacteria().getAttackDelay(), attackDelayCount, false)));
                if(attackDelayCount <= 0 || game.getState() != GameState.IN_GAME) {
                    cancel();
                    return;
                }
                attackDelayCount--;
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
