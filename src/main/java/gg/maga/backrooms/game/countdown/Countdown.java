package gg.maga.backrooms.game.countdown;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface Countdown {

    void onStart();
    void onCount();
    void onEnd();

    void onForceStop();

    void start();
    void stop(boolean force);
    boolean isRunning();

    int getCurrentCount();
    int getMaxCount();
}
