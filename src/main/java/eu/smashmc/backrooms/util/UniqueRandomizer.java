package eu.smashmc.backrooms.util;

import in.prismar.library.common.math.MathUtil;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class UniqueRandomizer {

    public static <T> T getRandom(T current, T[] array) {
        if(array.length <= 1) {
            return array[0];
        }
        T random = array[MathUtil.random(array.length - 1)];
        while (random.equals(current)) {
            random = array[MathUtil.random(array.length - 1)];
        }
        return random;
    }
}
