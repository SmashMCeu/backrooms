package eu.smashmc.backrooms.generator.strategy;

import eu.smashmc.backrooms.generator.BackroomsGenerator;
import eu.smashmc.backrooms.generator.strategy.result.GenerationResult;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public abstract class AbstractBackroomsStrategy<T extends GenerationResult> implements BackroomsStrategy<T> {

    private final BackroomsGenerator generator;

    public AbstractBackroomsStrategy(BackroomsGenerator generator) {
        this.generator = generator;
    }
}
