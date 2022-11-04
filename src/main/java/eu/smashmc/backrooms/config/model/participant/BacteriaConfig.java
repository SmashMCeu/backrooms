package eu.smashmc.backrooms.config.model.participant;

import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class BacteriaConfig {

    private float normalWalkSpeed = 0.23f;
    private float agroWalkSpeed = 0.3f;
    private float stunWalkSpeed = 0.13f;

    private int agroDuration = 10;
    private int stunDuration = 15;

    private int attackDelay = 3;

    private double damage = 10;

    private String agroSound = "custom:bacteria";
    private int agroSoundDelay = 12;
    private double agroSoundBlockDistance = 30;



}
