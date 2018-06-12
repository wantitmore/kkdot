package com.konka.alexa.display;


import com.konka.alexa.alexalib.interfaces.AvsItem;

public interface MusicProgressCallBack {

    void onProgressChange(AvsItem item, long offsetInMilliseconds, long duration);
    void onPlaystateChange();
}
