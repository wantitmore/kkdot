package com.willblaschko.android.alexavoicelibrary.display;


import com.willblaschko.android.alexa.interfaces.AvsItem;

public interface MusicProgressCallBack {

    void onProgressChange(AvsItem item, long offsetInMilliseconds, long duration);
}
