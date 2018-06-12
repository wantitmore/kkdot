package com.konka.alexa.alexalib.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by user001 on 2018-4-12.
 */

public class TimeUtil {
    public static long getAlertTime(String scheduleTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(scheduleTime).getTime();
        } catch (ParseException e) {
            Log.d("TimeUtil", "getAlertTime: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
