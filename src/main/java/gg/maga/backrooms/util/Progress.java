package gg.maga.backrooms.util;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Progress {

    private static final String TEMPLATE = "§8[§7■§7■§7■§7■§7■§7■§7■§7■§8] §70%";
    private static final long SPACERS = 10;
    private static final String CHARACTER = "§a■";

    public static String getProgress(long max, long current, boolean showNumber){
        String pattern = TEMPLATE;
        final long spacerReplaces = (current * SPACERS)/max;

        for (int i = 0; i < spacerReplaces; i++) {
            pattern = pattern.replaceFirst("§7■", CHARACTER);
        }
        if(showNumber) {
            pattern = pattern.replaceFirst("0", Long.toString((current * 100)/max));
        } else {
            pattern = pattern.replace(" §70%", "");
        }


        return pattern;
    }
}
