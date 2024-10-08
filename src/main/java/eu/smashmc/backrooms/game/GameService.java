package eu.smashmc.backrooms.game;

import eu.smashmc.api.SmashMc;
import eu.smashmc.api.vanish.Vanish;
import eu.smashmc.backrooms.command.sub.EntitySubCommand;
import eu.smashmc.backrooms.game.countdown.impl.EndCountdown;
import eu.smashmc.backrooms.game.countdown.impl.IngameCountdown;
import eu.smashmc.backrooms.game.event.*;
import eu.smashmc.backrooms.game.item.BackroomItemRegistry;
import eu.smashmc.backrooms.game.model.Game;
import eu.smashmc.backrooms.game.model.GameState;
import eu.smashmc.backrooms.game.participant.GameParticipant;
import eu.smashmc.backrooms.game.participant.entity.BacteriaParticipant;
import eu.smashmc.backrooms.game.participant.entity.EntityParticipant;
import eu.smashmc.backrooms.game.participant.lobby.LobbyParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistParticipant;
import eu.smashmc.backrooms.game.participant.scientist.ScientistState;
import eu.smashmc.backrooms.game.scoreboard.CustomBoardRegistry;
import eu.smashmc.backrooms.Backrooms;
import eu.smashmc.backrooms.BackroomsConstants;
import eu.smashmc.backrooms.config.ConfigProvider;
import eu.smashmc.backrooms.game.participant.spectator.SpectatorParticipant;
import eu.smashmc.backrooms.game.task.GameMainTask;
import eu.smashmc.backrooms.game.task.ParticipantKnockedTask;
import eu.smashmc.backrooms.util.raytrace.ScreenEntityFinder;
import eu.smashmc.lib.bukkit.world.SoundUtil;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameService {

    private static final Random RANDOM = new Random();

    @Inject
    private Backrooms backrooms;

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
            return Optional.of(game);
        }
        return Optional.empty();
    }

    /*
    public Optional<Game> findGame() {
        for (Game game : provider.getGames().values()) {
            if (game.getState() == GameState.LOBBY || game.getState() == GameState.IN_GAME) {
                if (game.getParticipantRegistry().getCount() < game.getProperties().getMaxPlayers()) {
                    return Optional.of(game);
                }
            }
        }
        return Optional.empty();
    }*/

    public void changeState(Game game, GameState state) {
        Bukkit.getPluginManager().callEvent(new GameChangeStateEvent(game, game.getState(), state));
        game.setState(state);
    }

    public ScientistParticipant spectate(Player player, Game game, ScientistParticipant spectating) {
        ScientistParticipant spectate = null;
        if (spectating != null) {
            if (spectating.getState() != ScientistState.ALIVE) {
                player.setSpectatorTarget(null);
            }
        }
        if (player.getSpectatorTarget() == null) {
            player.setGameMode(GameMode.SPECTATOR);
            int alive = getAliveParticipants(game);
            if (alive == 1) {
                spectate = findRandomScientist(game);
            } else if (alive >= 2) {
                spectate = spectating == null ? findRandomScientist(game) :
                        findRandomAliveScientistExcept(game, spectating.getPlayer());
            }
        } else {
            if (spectating != null) {
                if (spectating.getState() != ScientistState.ALIVE) {
                    spectate = findRandomScientist(game);
                }
            }
        }
        if (spectate != null) {
            player.setSpectatorTarget(spectate.getPlayer());
            sendSpectatingTitle(player, spectate.getPlayer().getName());
        }

        return spectate;
    }

    private void sendSpectatingTitle(Player player, String target) {
        player.sendTitle("", "§7Now spectating §e" + target, 20, 20, 20);
        player.sendMessage(BackroomsConstants.PREFIX + "§eShift §7to switch your spectating target.");
    }

    public void beginGame(Game game) {
        changeState(game, GameState.IN_GAME);
        backrooms.getGameState().setToIngame();
        game.setCountdown(new IngameCountdown(backrooms, provider, this, game));
        game.getCountdown().start();
        shuffleParticipants(game);
        int scientists = getAliveParticipants(game);
        game.getProperties().setMaxTasks(configProvider.getEntity().getGame().getTasksPerPlayer() * scientists);
        game.getProperties().setMaxScientists(scientists);
        executeForAll(game, participant -> {
            resetPlayer(participant.getPlayer(), GameMode.ADVENTURE);
            Player player = participant.getPlayer();

            if (participant instanceof EntityParticipant entity) {
                Location location = game.getMap().getRandomEntitySpawn();
                player.teleport(location);
                player.sendTitle("§4Entity", "§c" + entity.getName(), 20, 40, 20);
                player.playSound(player.getLocation(), Sound.BLOCK_NETHERRACK_BREAK, 1, 1);

                player.sendMessage(" ");
                player.sendMessage(CenteredMessage.createCentredMessage("§4§lEntity"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7Your goal is to kill all §eScientists"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7before they escape."));
                player.sendMessage(" ");

                player.getInventory().setItem(4, itemRegistry.createItem("Reveal"));

            } else if (participant instanceof ScientistParticipant) {
                Location location = game.getMap().getRandomScientistSpawn();
                player.teleport(location);
                player.sendTitle("§eScientist", "", 20, 40, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                player.sendMessage(" ");
                player.sendMessage(CenteredMessage.createCentredMessage("§e§lScientist"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7Your goal is to solve §a" + game.getProperties().getMaxTasks() + " §7tasks."));
                player.sendMessage(CenteredMessage.createCentredMessage("§7These tasks are distributed on the map, after you solve"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7each Task, §d2 §7Gates will be opened. §7You win"));
                player.sendMessage(CenteredMessage.createCentredMessage("§7if you successfully escape the backrooms."));
                player.sendMessage(" ");
            } else if (participant instanceof SpectatorParticipant) {
                player.teleport(game.getMap().getRandomScientistSpawn());
            }
            getBoardRegistry().resetSidebar(player);
            participant.disableJump();

        });
        backrooms.getStats().startGame(configProvider.getEntity().getGame().getMapName(), game.getParticipantRegistry().getParticipants().values());
        sendMessage(game, BackroomsConstants.PREFIX + "§7The §agame §7has started");
        startMainTask(game);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(game));
    }

    public void solveTask(Game game, GameParticipant solver) {

        int tasks = game.getSolvedTasks() + 1;
        if (tasks <= game.getProperties().getMaxTasks()) {
            game.setSolvedTasks(tasks);
            backrooms.getStats().addTaskSolve(solver.getPlayer());
            executeForAll(game, participant -> {
                participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.BLOCK_PISTON_EXTEND, 0.45F, 1);
                if (participant instanceof EntityParticipant) {
                    participant.getPlayer().sendTitle("§cTask solved", "", 20, 20, 20);
                } else {
                    participant.getPlayer().sendTitle("§aTask solved", "", 20, 20, 20);
                }

            });
        }
        if (tasks == game.getProperties().getMaxTasks()) {
            executeForAll(game, participant -> {
                participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.45F, 1);
                participant.getPlayer().sendTitle("§eThreshold", "§7has been opened", 20, 40, 20);
            });
            openThreshold(game);
        }
    }

    public void knock(Game game, EntityParticipant entity, ScientistParticipant participant) {
        if (participant.getKnocks() <= 0) {
            kill(game, participant);
            if (entity != null) {
                backrooms.getStats().addKill(entity.getPlayer());
            }
        } else {
            if (entity != null) {
                participant.setKnockedBy(entity);
            }
            participant.setKnocks(participant.getKnocks() - 1);
            participant.setState(ScientistState.KNOCKED);
            participant.setKnockedLocation(participant.getPlayer().getLocation());
            participant.setKnockedUntil(System.currentTimeMillis() + 1000 * 60 * 2);
            participant.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
            new ParticipantKnockedTask(game, this, provider, participant).runTaskTimer(provider.getBackrooms(), 20, 20);
            sendMessage(game, BackroomsConstants.PREFIX + "§e" + participant.getPlayer().getName() + " §7has been knocked.");
        }
        int alive = getAliveParticipants(game);
        if (alive <= 0) {
            endGame(game);
        }
    }

    public void kill(Game game, ScientistParticipant participant) {
        if (participant.getKnockedHologram() != null) {
            participant.getKnockedHologram().disable();
            participant.setKnockedHologram(null);
        }
        String killedBy = "has died.";
        if (participant.getKnockedBy() != null) {
            backrooms.getStats().addKill(participant.getKnockedBy().getPlayer());
            participant.getKnockedBy().getPlayer().playSound(participant.getKnockedBy().getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.4f);
            killedBy = "was killed by §4Entity";
        }
        participant.getPlayer().sendTitle("§cYou died", "", 20, 20, 20);
        participant.setState(ScientistState.DEAD);
        participant.getPlayer().setGameMode(GameMode.SPECTATOR);

        sendMessage(game, BackroomsConstants.PREFIX + "§e" + participant.getPlayer().getName() + " §7" + killedBy);
        backrooms.getStats().addDeath(participant.getPlayer());
        int alive = getAliveParticipants(game);
        if (alive <= 0) {
            endGame(game);
        } else {
            ScientistParticipant random = findRandomScientist(game);
            participant.setSpectating(random);
            participant.getPlayer().setSpectatorTarget(random.getPlayer());
        }
    }

    public void escape(Game game, ScientistParticipant scientist) {
        Player player = scientist.getPlayer();
        scientist.setState(ScientistState.ESCAPED);
        resetPlayer(player, GameMode.ADVENTURE);
        player.teleport(provider.getConfigProvider().getEntity().getGame().getLobby());
        int escaped = getEscapedParticipants(game);
        int alive = getAliveParticipants(game);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.6f, 1);
        sendMessage(game, BackroomsConstants.PREFIX + "§e" + player.getName() + " §7has escaped. §8[§e" + escaped + "/§e" + game.getProperties().getMaxScientists() + "§8]");
        backrooms.getStats().addScientistWin(scientist.getPlayer());
        if (alive <= 0) {
            endGame(game);
        }
    }

    public void revive(Game game, ScientistParticipant reviver, ScientistParticipant participant) {
        participant.setState(ScientistState.ALIVE);
        for (PotionEffect effect : participant.getPlayer().getActivePotionEffects()) {
            participant.getPlayer().removePotionEffect(effect.getType());
        }
        participant.getPlayer().setHealth(10);
        participant.getKnockedHologram().disable();
        participant.setKnockedHologram(null);
        participant.setKnockedBy(null);
        participant.getPlayer().getWorld().playSound(participant.getKnockedLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1);
        participant.getPlayer().sendTitle("§7You have been §arevived", "", 20, 20, 20);
        sendMessage(game, BackroomsConstants.PREFIX + "§e" + participant.getPlayer().getName() + " §7has been revived.");
        participant.disableJump();
    }

    public GameParticipant findRandomParticipantExcept(Game game, Player player) {
        GameParticipant[] participants = game.getParticipantRegistry().getParticipants().values().toArray(new GameParticipant[0]);
        GameParticipant chosen = participants[RANDOM.nextInt(game.getParticipantRegistry().getCount())];
        while (chosen.getPlayer().getName().equals(player.getName())) {
            chosen = participants[MathUtil.random(participants.length - 1)];
        }
        return chosen;
    }

    public ScientistParticipant findNearestParticipant(Player player, Game game) {
        ScientistParticipant nearest = null;
        double distance = Double.MAX_VALUE;
        for (GameParticipant participant : game.getParticipantRegistry().getParticipants().values()) {
            if (participant instanceof ScientistParticipant scientist) {
                if (scientist.getState() == ScientistState.ALIVE) {
                    double scientistDistance = scientist.getPlayer().getLocation().distanceSquared(player.getLocation());
                    if (scientistDistance <= distance) {
                        distance = scientistDistance;
                        nearest = scientist;
                    }
                }
            }
        }
        return nearest;
    }

    public ScientistParticipant findRandomScientist(Game game) {
        List<GameParticipant> participants = game.getParticipantRegistry().getParticipants().values()
                .stream().filter(participant -> {
                    if (participant instanceof ScientistParticipant scientist) {
                        return scientist.getState() == ScientistState.ALIVE;
                    }
                    return false;
                }).toList();
        return (ScientistParticipant) participants.get(MathUtil.random(participants.size() - 1));
    }

    public ScientistParticipant findRandomAliveScientistExcept(Game game, Player player) {
        List<GameParticipant> participants = game.getParticipantRegistry().getParticipants().values()
                .stream().filter(participant -> {
                    if (participant instanceof ScientistParticipant scientist) {
                        return scientist.getState() == ScientistState.ALIVE;
                    }
                    return false;
                }).toList();
        ScientistParticipant randomPart = (ScientistParticipant) participants.get(MathUtil.random(participants.size() - 1));
        while (randomPart.getPlayer().getName().equals(player.getName())) {
            randomPart = (ScientistParticipant) participants.get(MathUtil.random(participants.size() - 1));
        }
        return randomPart;
    }

    public int getEntityParticipants(Game game) {
        List<GameParticipant> participants = game.getParticipantRegistry().getParticipants().values()
                .stream().filter(participant -> {
                    if (participant instanceof EntityParticipant) {
                        return true;
                    }
                    return false;
                }).toList();
        return participants.size();
    }

    public int getAliveParticipants(Game game) {
        List<GameParticipant> participants = game.getParticipantRegistry().getParticipants().values()
                .stream().filter(participant -> {
                    if (participant instanceof ScientistParticipant scientist) {
                        if (scientist.getState() == ScientistState.ALIVE) {
                            return true;
                        }
                    }
                    return false;
                }).toList();
        return participants.size();
    }

    public int getEscapedParticipants(Game game) {
        List<GameParticipant> participants = game.getParticipantRegistry().getParticipants().values()
                .stream().filter(participant -> {
                    if (participant instanceof ScientistParticipant scientist) {
                        if (scientist.getState() == ScientistState.ESCAPED) {
                            return true;
                        }
                    }
                    return false;
                }).toList();
        return participants.size();
    }


    public void endGame(Game game) {
        if (game.getCountdown().isRunning()) {
            game.getCountdown().stop(true);
        }
        changeState(game, GameState.END);

        int entities = getEntityParticipants(game);
        int escaped = getEscapedParticipants(game);

        boolean entityWin = escaped <= 0 && entities >= 1;

        executeForAll(game, participant -> {
            resetPlayer(participant.getPlayer(), GameMode.ADVENTURE);
            if (participant instanceof EntityParticipant) {
                DisguiseAPI.undisguiseToAll(participant.getPlayer());
                if (entityWin) {
                    backrooms.getStats().addEntityWin(participant.getPlayer());
                }
            } else if (participant instanceof ScientistParticipant scientist) {
                if (scientist.getState() == ScientistState.KNOCKED) {
                    if (scientist.getKnockedHologram() != null) {
                        scientist.getKnockedHologram().disable();
                    }
                    if (scientist.getKnockedBy() != null) {
                        backrooms.getStats().addKill(scientist.getKnockedBy().getPlayer());
                    }
                }
            }
            participant.getPlayer().teleport(backrooms.getConfigProvider().getEntity().getGame().getLobby());
            participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1);
        });
        backrooms.getStats().endGame();
        //TODO: Find Winner
        game.setCountdown(new EndCountdown(backrooms, provider, this, game));
        game.getCountdown().start();
        game.getMainTask().cancel();

        sendMessage(game, " ");
        if (entityWin) {

            sendMessage(game, CenteredMessage.createCentredMessage("§4Entities"));
            sendMessage(game, CenteredMessage.createCentredMessage("§7have won this round."));
        } else {
            sendMessage(game, CenteredMessage.createCentredMessage("§eScientist"));
            sendMessage(game, CenteredMessage.createCentredMessage("§7have won this round."));
            sendMessage(game, CenteredMessage.createCentredMessage("§e" + escaped + " §7out of §e" + game.getProperties().getMaxScientists() + " scientists §7escaped."));
        }
        sendMessage(game, " ");


        Bukkit.getPluginManager().callEvent(new GameEndEvent(game));
    }

    public void startMainTask(Game game) {
        game.setMainTask(new GameMainTask(provider, this, game).runTaskTimer(getProvider().getBackrooms(), 20, 20));
    }

    public List<GameParticipant> raytraceParticipants(Game game, Player player) {
        List<GameParticipant> participants = new ArrayList<>();
        List<Entity> entities = new ScreenEntityFinder(player).findEntities(24, 60);
        for (Entity entity : entities) {
            if (entity instanceof Player target) {
                GameParticipant targetParticipant = game.getParticipantRegistry().getParticipant(target.getUniqueId());
                participants.add(targetParticipant);
            }
        }
        return participants;
    }

    public void joinGameAsSpectator(Game game, Player player) {
        resetPlayer(player, GameMode.SPECTATOR);
        game.getParticipantRegistry().register(player.getUniqueId(), new SpectatorParticipant(player));

        GameParticipant participant = findRandomParticipantExcept(game, player);
        player.teleport(participant.getPlayer().getLocation());

        boardRegistry.add(player, game);
        boardRegistry.updateTablistAll();
    }

    public void joinGame(Game game, Player player) {
        game.getParticipantRegistry().register(player.getUniqueId(), new LobbyParticipant(player));
        player.teleport(configProvider.getEntity().getGame().getLobby());
        resetPlayer(player, GameMode.ADVENTURE);
        backrooms.getGameState().setIngamePlayers(game.getParticipantRegistry().getCount());
        if (game.getParticipantRegistry().getCount() >= game.getProperties().getMinPlayers()) {
            if (!game.getCountdown().isRunning()) {
                game.getCountdown().start();
            }
        }

        boardRegistry.add(player, game);
        boardRegistry.updateTablistAll();

        giveLobbyItems(player);


    }

    public void leaveGame(Game game, Player player, boolean force) {
        if (force) {
            player.teleport(configProvider.getEntity().getGame().getLobby());
            resetPlayer(player, GameMode.SURVIVAL);
        } else {
            player.teleport(configProvider.getEntity().getGame().getLobby());
            resetPlayer(player, GameMode.SURVIVAL);
            GameParticipant participant = game.getParticipantRegistry().unregister(player.getUniqueId());
            backrooms.getGameState().setIngamePlayers(game.getParticipantRegistry().getCount());
            if (game.getState() == GameState.IN_GAME) {
                if (participant instanceof ScientistParticipant scientist) {
                    if (scientist.getState() == ScientistState.KNOCKED && scientist.getKnockedBy() != null) {
                        backrooms.getStats().addKill(scientist.getKnockedBy().getPlayer());
                    }
                }
                if (game.getParticipantRegistry().getParticipants().size() <= 1) {
                    endGame(game);
                    return;
                }
                int entities = getEntityParticipants(game);
                if (entities == 0) {
                    endGame(game);
                    return;
                }
            } else if (game.getState() == GameState.LOBBY) {
                if (game.getParticipantRegistry().getCount() < game.getProperties().getMinPlayers()) {
                    if (game.getCountdown().isRunning()) {
                        game.getCountdown().stop(true);
                    }
                }
            }
        }
        boardRegistry.remove(player);
        boardRegistry.updateTablistAll();

    }

    public void openThreshold(Game game) {
        for (Block block : game.getMap().getPortalBlocks()) {
            block.setType(GameConstants.PORTAL_BLOCK);
        }
    }

    public void shuffleParticipants(Game game) {
        Vanish<Player> vanish = SmashMc.getComponent(Vanish.class);
        GameParticipant[] participants = game.getParticipantRegistry().getParticipants().values().stream()
                .filter(gameParticipant -> !vanish.isVanished(gameParticipant.getPlayer())).toArray(GameParticipant[]::new);

        int entities = game.getProperties().getMaxEntities();
        if (EntitySubCommand.ENFORCED_ENTITY != null) {
            entities--;
            game.getParticipantRegistry().register(EntitySubCommand.ENFORCED_ENTITY, getRandomEntity(Bukkit.getPlayer(EntitySubCommand.ENFORCED_ENTITY)));
        }
        for (int i = 0; i < entities; i++) {
            int random = MathUtil.random(game.getParticipantRegistry().getCount() - 1);
            GameParticipant entityParticipant = participants[random];
            game.getParticipantRegistry().register(entityParticipant.getPlayer().getUniqueId(), getRandomEntity(entityParticipant.getPlayer()));
            participants[random] = null;
        }
        for (int i = 0; i < participants.length; i++) {
            GameParticipant participant = participants[i];
            if (participant != null) {
                if (EntitySubCommand.ENFORCED_ENTITY != null) {
                    if (participant.getPlayer().getUniqueId().equals(EntitySubCommand.ENFORCED_ENTITY)) {
                        continue;
                    }
                }
                game.getParticipantRegistry().register(participant.getPlayer().getUniqueId(), new ScientistParticipant(participant.getPlayer()));
            }
        }

        GameParticipant[] vanishedParticipants = game.getParticipantRegistry().getParticipants().values().stream()
                .filter(gameParticipant -> vanish.isVanished(gameParticipant.getPlayer())).toArray(GameParticipant[]::new);
        for (GameParticipant vanished : vanishedParticipants) {
            joinGameAsSpectator(game, vanished.getPlayer());
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
