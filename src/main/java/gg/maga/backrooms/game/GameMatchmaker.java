package gg.maga.backrooms.game;

import gg.maga.backrooms.BackroomsConstants;
import gg.maga.backrooms.config.ConfigProvider;
import gg.maga.backrooms.game.countdown.impl.EndCountdown;
import gg.maga.backrooms.game.countdown.impl.IngameCountdown;
import gg.maga.backrooms.game.event.GameEndEvent;
import gg.maga.backrooms.game.event.GameJoinEvent;
import gg.maga.backrooms.game.event.GameLeaveEvent;
import gg.maga.backrooms.game.event.GameStartEvent;
import gg.maga.backrooms.game.model.Game;
import gg.maga.backrooms.game.task.GameMainTask;
import gg.maga.backrooms.game.model.GameState;
import gg.maga.backrooms.game.participant.GameParticipant;
import gg.maga.backrooms.game.participant.entity.BacteriaParticipant;
import gg.maga.backrooms.game.participant.entity.EntityParticipant;
import gg.maga.backrooms.game.participant.lobby.LobbyParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistParticipant;
import gg.maga.backrooms.game.participant.scientist.ScientistState;
import gg.maga.backrooms.game.scoreboard.CustomBoardRegistry;
import gg.maga.backrooms.game.item.BackroomItemRegistry;
import gg.maga.backrooms.game.task.ParticipantKnockedTask;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.text.CenteredMessage;
import lombok.Getter;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameMatchmaker {

    @Inject
    private GameProvider provider;

    @Inject
    private ConfigProvider configProvider;

    @Inject
    private CustomBoardRegistry boardRegistry;

    @Inject
    private BackroomItemRegistry itemRegistry;

    public Optional<Game> findGame() {
        for (Game game : provider.getGames().values()) {
            if (game.getState() == GameState.LOBBY) {
                if (game.getParticipantRegistry().getCount() < game.getProperties().getMaxPlayers()) {
                    return Optional.of(game);
                }
            }
        }
        return Optional.empty();
    }

    public void beginGame(Game game) {
        GameProvider provider = game.getProvider();
        GameMatchmaker matchmaker = game.getProvider().getMatchmaker();

        provider.changeState(game, GameState.IN_GAME);
        game.setCountdown(new IngameCountdown(game));
        game.getCountdown().start();
        matchmaker.shuffleParticipants(game);
        matchmaker.executeForAll(game, participant -> {
            matchmaker.resetPlayer(participant.getPlayer(), GameMode.ADVENTURE);
            Player player = participant.getPlayer();
            for (int i = 0; i < 200; i++) {
                player.sendMessage(" ");
            }

            if (participant instanceof EntityParticipant entity) {
                Location location = game.getMap().getRandomEntitySpawn();
                player.teleport(location);
                player.sendTitle("§4Entity", "§c" + entity.getName(), 20, 40, 20);
                player.playSound(player.getLocation(), Sound.BLOCK_NETHERRACK_BREAK, 1, 1);

                player.sendMessage(CenteredMessage.createCentredMessage("§4§lEntity"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7Your goal is to kill all §eScientists"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7before they escape."));
                player.sendMessage(" ");

            } else if (participant instanceof ScientistParticipant) {
                player.getInventory().addItem(matchmaker.getItemRegistry().createItem("Almond Water"));
                Location location = game.getMap().getRandomScientistSpawn();
                player.teleport(location);
                player.sendTitle("§eScientist", "", 20, 40, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                player.sendMessage(CenteredMessage.createCentredMessage("§e§lScientist"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7Your goal is to solve §a" + game.getProperties().getMaxTasks() + " §7tasks."));
                player.sendMessage(CenteredMessage.createCentredMessage("§7These tasks are distributed on the map, after you solve"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7each Task, §d2 §7Gates will be opened. §7You win"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7if you successfully escape the backrooms."));
                player.sendMessage(" ");
            }
            matchmaker.getBoardRegistry().resetSidebar(player);

        });
        matchmaker.sendMessage(game, BackroomsConstants.PREFIX + "§7The §agame §7has started");
        startMainTask(game);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(game));
    }

    public void solveTask(Game game) {
        int tasks = game.getSolvedTasks() + 1;
        if (tasks <= game.getProperties().getMaxTasks()) {
            game.setSolvedTasks(tasks);
            game.getProvider().getMatchmaker().executeForAll(game, participant -> {
                participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.BLOCK_PISTON_EXTEND, 0.45F, 1);
                participant.getPlayer().sendTitle("§aTask solved", "", 20, 20, 20);
            });
        }
        if (tasks == game.getProperties().getMaxTasks()) {
            game.getProvider().getMatchmaker().executeForAll(game, participant -> {
                participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.45F, 1);
                participant.getPlayer().sendTitle("§eThreshold", "§7has been opened", 20, 40, 20);
            });
            openThreshold(game);
        }
    }

    public void knock(Game game, ScientistParticipant participant) {
        if (participant.getKnocks() <= 0) {
            participant.getPlayer().sendTitle("§cYou died", "", 20, 20, 20);

            participant.setState(ScientistState.DEAD);
            participant.getPlayer().setGameMode(GameMode.SPECTATOR);
            int alive = getAliveParticipants(game);
            if(alive <= 0) {
                endGame(game);
            } else {
                ScientistParticipant random = findRandomScientist(game);
                participant.setSpectating(random);
                participant.getPlayer().setSpectatorTarget(random.getPlayer());
            }
        } else {
            participant.setKnocks(participant.getKnocks() - 1);
            participant.setState(ScientistState.KNOCKED);
            participant.setKnockedLocation(participant.getPlayer().getLocation());
            participant.setKnockedTimestamp(System.currentTimeMillis());
            participant.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            new ParticipantKnockedTask(game, participant).runTaskTimer(provider.getBackrooms(), 20, 20);
        }
    }

    public void revive(Game game, ScientistParticipant reviver, ScientistParticipant participant) {
        participant.setState(ScientistState.ALIVE);
        for(PotionEffect effect : participant.getPlayer().getActivePotionEffects()) {
            participant.getPlayer().removePotionEffect(effect.getType());
        }
    }

    public ScientistParticipant findRandomScientist(Game game) {
        List<GameParticipant> participants = game.getParticipantRegistry().getParticipants().values()
                .stream().filter(participant -> participant instanceof ScientistParticipant).toList();
        ScientistParticipant randomPart = (ScientistParticipant) participants.get(MathUtil.random(participants.size() - 1));
        while (randomPart.getState() == ScientistState.KNOCKED) {
            randomPart = (ScientistParticipant) participants.get(MathUtil.random(participants.size() - 1));
        }
        return randomPart;
    }

    public int getAliveParticipants(Game game) {
        List<GameParticipant> participants = game.getParticipantRegistry().getParticipants().values()
                .stream().filter(participant -> {
                    if(participant instanceof ScientistParticipant scientist) {
                        if(scientist.getState() == ScientistState.ALIVE) {
                            return true;
                        }
                    }
                    return false;
                }).toList();
        return participants.size();
    }

    public void endGame(Game game) {
        game.getProvider().changeState(game, GameState.END);
        game.getProvider().getMatchmaker().executeForAll(game, participant -> {
            game.getProvider().getMatchmaker().resetPlayer(participant.getPlayer(), GameMode.ADVENTURE);
            if(participant instanceof EntityParticipant) {
                DisguiseAPI.undisguiseToAll(participant.getPlayer());
            }
        });
        //TODO: Find Winner
        game.setCountdown(new EndCountdown(game));
        game.getCountdown().start();
        game.getMainTask().cancel();
        Bukkit.getPluginManager().callEvent(new GameEndEvent(game));
    }

    public void startMainTask(Game game) {
        game.setMainTask(new GameMainTask(game).runTaskTimer(getProvider().getBackrooms(), 20, 20));
    }

    public void joinGame(Game game, Player player) {
        GameJoinEvent event = new GameJoinEvent(game, player);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled() && game.getParticipantRegistry().getCount() < game.getProperties().getMaxPlayers()) {
            game.getParticipantRegistry().register(player.getUniqueId(), new LobbyParticipant(player));
            player.teleport(configProvider.getEntity().getGame().getLobby());
            resetPlayer(player, GameMode.ADVENTURE);

            if (game.getParticipantRegistry().getCount() >= game.getProperties().getMaxPlayers()) {
                if (!game.getCountdown().isRunning()) {
                    game.getCountdown().start();
                }
            }

            boardRegistry.add(player, game);
            boardRegistry.updateTablistAll();

            giveLobbyItems(player);
        }

    }

    public void leaveGame(Game game, Player player, boolean force) {
        if (force) {
            player.teleport(configProvider.getEntity().getGame().getLobby());
            resetPlayer(player, GameMode.SURVIVAL);
        } else {
            GameLeaveEvent event = new GameLeaveEvent(game, player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                player.teleport(configProvider.getEntity().getGame().getLobby());
                resetPlayer(player, GameMode.SURVIVAL);
                game.getParticipantRegistry().unregister(player.getUniqueId());
                if (game.getParticipantRegistry().getCount() < game.getProperties().getMaxPlayers()) {
                    if (game.getCountdown().isRunning()) {
                        game.getCountdown().stop(true);
                    }
                }
                boardRegistry.remove(player);
                boardRegistry.updateTablistAll();
            }
        }

    }

    public void openThreshold(Game game) {
        for (Block block : game.getMap().getPortalBlocks()) {
            block.setType(GameConstants.PORTAL_BLOCK);
        }
    }

    public void shuffleParticipants(Game game) {
        GameParticipant[] participants = game.getParticipantRegistry().getParticipants().values().toArray(new GameParticipant[0]);

        int entities = game.getProperties().getMaxEntities();
        for (int i = 0; i < entities; i++) {
            int random = MathUtil.random(game.getParticipantRegistry().getCount() - 1);
            GameParticipant entityParticipant = participants[random];
            game.getParticipantRegistry().register(entityParticipant.getPlayer().getUniqueId(),
                    getRandomEntity(entityParticipant.getPlayer()));
            participants[random] = null;
        }
        for (int i = 0; i < participants.length; i++) {
            GameParticipant participant = participants[i];
            if (participant != null) {
                game.getParticipantRegistry().register(participant.getPlayer().getUniqueId(), new ScientistParticipant(participant.getPlayer()));
            }
        }
    }

    public EntityParticipant getRandomEntity(Player player) {
        return new BacteriaParticipant(player);
    }


    public void resetPlayer(Player player, GameMode mode) {
        player.setGameMode(mode);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setWalkSpeed(0.2f);
        player.setLevel(0);
        player.setExp(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.getInventory().clear();
    }

    public void giveLobbyItems(Player player) {
        player.getInventory().setItem(8, itemRegistry.createItem("Leave"));
    }

    public void sendMessage(Game game, String message) {
        for (GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            participant.getPlayer().sendMessage(message);
        }
    }

    public void executeForAll(Game game, Consumer<GameParticipant> consumer) {
        for (GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            consumer.accept(participant);
        }
    }

    public Optional<Game> getGameByPlayer(Player player) {
        for (Game game : provider.getGames().values()) {
            if (game.getParticipantRegistry().existsParticipant(player.getUniqueId())) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }


    public boolean isInGame(Player player) {
        return getGameByPlayer(player).isPresent();
    }
}
