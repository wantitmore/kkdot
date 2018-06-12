package com.konka.alexa.utility;


import java.util.Formatter;
import java.util.Locale;

public class TimeFormatUitls {

    private static StringBuilder mFormatBuilder = new StringBuilder();
    private static  Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    public static String formatTime(int timeMs) {

        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;

        mFormatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d", minutes, seconds)
                .toString();
    }
}
