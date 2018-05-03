package com.willblaschko.android.alexavoicelibrary.utility;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;


/**
 * Created by user001 on 2018-4-27.
 */

public class CommonUtil {
    public static String getTopActivity(Context context)

    {

        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE) ;

        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;

        if(runningTaskInfos != null && runningTaskInfos.size() > 0)

            return (runningTaskInfos.get(0).topActivity.getClassName()) ;

        else

            return null ;

    }
}
