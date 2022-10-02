package gg.maga.backrooms.generator.exception;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class MissingStrategyException extends Exception {

    public MissingStrategyException() {
        super("Please set the strategy first before you generate");
    }
}
