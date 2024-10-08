package eu.smashmc.backrooms.config.model;

import eu.smashmc.backrooms.config.model.participant.BacteriaConfig;
import eu.smashmc.backrooms.config.model.participant.ParticipantConfig;
import eu.smashmc.backrooms.config.model.participant.ScientistConfig;
import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class BackroomsConfig {

    private boolean build;
    private GeneratorConfig generator;
    private GameConfig game;
    private ParticipantConfig participant;

}
