package com.konka.alexa.utility;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import com.konka.alexa.display.PlayInfoActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.USAGE_STATS_SERVICE;


/**
 * Created by user001 on 2018-4-27.
 */

public class CommonUtil {

    private static final String TAG = "CommonUtil";
    public static int getTopActivity(Context context) {
        return PlayInfoActivity.isTop;
    }


    public static String getForegroundApp(Context context) {
        try {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date());
            long endt = calendar.getTimeInMillis();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            long statt = calendar.getTimeInMillis();
            UsageStatsManager usageStatsManager=(UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);

            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY,statt,endt);

            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                return null;
            }

            UsageStats recentStats = null;
            for (UsageStats usageStats : queryUsageStats) {

                if(recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()){
                    recentStats = usageStats;
                }
            }
            Log.d(TAG, "getForegroundApp: " + recentStats.getPackageName());
            return recentStats.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
