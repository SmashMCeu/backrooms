package eu.smashmc.backrooms.game.item.event;

import eu.smashmc.backrooms.game.item.BackroomItem;
import eu.smashmc.backrooms.game.GameProvider;
import eu.smashmc.backrooms.game.GameService;
import eu.smashmc.backrooms.game.model.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BackroomItemEventBus {

    private final BackroomItem item;
    private Map<Class<?>, List<Method>> subscribers;

    public BackroomItemEventBus(BackroomItem item) {
        this.item = item;
        this.subscribers = new HashMap<>();
        scan();
    }

    public void publish(Player player, GameProvider provider, GameService service, Game game, Object event) {
        if(subscribers.containsKey(event.getClass())) {
            List<Method> methods = subscribers.get(event.getClass());
            for(Method method : methods) {
                try {
                    method.invoke(item, player, provider, service, game, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void scan() {
        Class<?> clazz = item.getClass();
        for(Method method : clazz.getMethods()) {
            if(method.isAnnotationPresent(BackroomItemEvent.class)) {
                Parameter parameter = method.getParameters()[4];
                Class<?> type = parameter.getType();
                if(!subscribers.containsKey(type)) {
                    subscribers.put(type, new ArrayList<>());
                }
                subscribers.get(type).add(method);
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public class BackroomItemEventArgs<T> {

        private Player player;
        private GameProvider provider;
        private GameService service;
        private T event;
    }
}
