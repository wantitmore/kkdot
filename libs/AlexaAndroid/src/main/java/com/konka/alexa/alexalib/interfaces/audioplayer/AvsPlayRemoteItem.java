package com.konka.alexa.alexalib.interfaces.audioplayer;

import com.konka.alexa.alexalib.interfaces.AvsItem;

/**
 * Directive to play a remote URL item
 *
 * {@link com.konka.alexa.alexalib.data.Directive} response item type parsed so we can properly
 * deal with the incoming commands from the Alexa server.
 *
 * @author will on 5/21/2016.
 */
public class AvsPlayRemoteItem extends AvsItem {
    public final static String PLAYER_ACTIVITY_IDLE = "IDLE";
    public final static String PLAYER_ACTIVITY_PLAYING = "PLAYING";
    public final static String PLAYER_ACTIVITY_FINISHED = "FINISHED";
    public final static String PLAYER_ACTIVITY_PAUSED = "PAUSED";
    public final static String PLAYER_ACTIVITY_STOPPED = "STOPPED";

    private String mUrl;
    private String mStreamId;
    private long mStartOffset;
    private String mPlayerActivity;
    long mProgressReportDelayInMilliseconds;
    long mProgressReportIntervalInMilliseconds;
    Boolean mNeedResumed;

    public AvsPlayRemoteItem(String token, String url, long startOffset,long progressReportDelayInMilliseconds,long progressReportIntervalInMilliseconds) {
        super(token);
        mUrl = url;
        mStartOffset = (startOffset < 0) ? 0 : startOffset;
        mPlayerActivity = PLAYER_ACTIVITY_IDLE;
        mProgressReportDelayInMilliseconds =progressReportDelayInMilliseconds;
        mProgressReportIntervalInMilliseconds = progressReportIntervalInMilliseconds;
        mNeedResumed = false;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setStartoffset(long startOffset) {
        mStartOffset = startOffset;
    }

    public long getStartOffset() {
        return mStartOffset;
    }

    public void setPlayerActivity(String activity){  mPlayerActivity = activity; }

    public String getPlayerAcivity(){ return mPlayerActivity; }

    public long getProgressReportDelayInMilliseconds(){
        return mProgressReportDelayInMilliseconds;
    }

    public long getmProgressReportIntervalInMilliseconds(){
        return mProgressReportIntervalInMilliseconds;
    }

    public Boolean isNeedResumed(){
        return mNeedResumed;
    }

    public void SetNeedResumed(Boolean needResumed){
        mNeedResumed = needResumed;
    }
}
