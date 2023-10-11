package eu.smashmc.backrooms.game.item.impl;

import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.item.event.BackroomItemEvent;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
import eu.smashmc.backrooms.util.Progress;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class MedkitBackroomItem extends BackroomItem {

    public MedkitBackroomItem() {
        super("Medkit", Material.TOTEM_OF_UNDYING);
        setDisplayName("§cMedkit");
        addLore("§7You can revive someone instantly");
    }

    @BackroomItemEvent
    public void onInteract(Player player, GameProvider provider, GameService service,
                          Game game, PlayerInteractEvent event) {
        event.setCancelled(true);
        for(GameParticipant otherParticipants : game.getParticipantRegistry().getParticipants().values()) {
            if(otherParticipants instanceof ScientistParticipant scientist ) {
                if(scientist.getState() == ScientistState.KNOCKED && !scientist.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                    if(scientist.getPlayer().getLocation().distanceSquared(player.getLocation()) <= 3) {
                        if(event.getHand() == EquipmentSlot.HAND) {
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        } else if(event.getHand() == EquipmentSlot.OFF_HAND) {
                            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 2);
                        player.updateInventory();
                        service.revive(game, game.getParticipantRegistry().getParticipant(player.getUniqueId()), scientist);
                        return;
                    }
                }
            }
        }
        player.sendMessage(BackroomsConstants.PREFIX + "§cYou have nobody to revive nearby");
    }
}
