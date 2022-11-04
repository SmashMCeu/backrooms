package eu.smashmc.backrooms.config.model.participant;

import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class ParticipantConfig {

    public static ParticipantConfig create() {
        ParticipantConfig config = new ParticipantConfig();
        config.setScientist(new ScientistConfig());
        config.setBacteria(new BacteriaConfig());
        return config;
    }

    private ScientistConfig scientist;
    private BacteriaConfig bacteria;


}
