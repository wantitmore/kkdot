package com.konka.alexa.alexalib.interfaces.speechsynthesizer;


import com.konka.alexa.alexalib.interfaces.AvsItem;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Directive to play a local, returned audio item from the Alexa post/get response
 *
 * {@link com.konka.alexa.alexalib.data.Directive} response item type parsed so we can properly
 * deal with the incoming commands from the Alexa server.
 *
 * @author will on 5/21/2016.
 */
public class AvsSpeakItem extends AvsItem {
    public final static String PLAYER_ACTIVITY_PLAYING = "PLAYING";
    public final static String PLAYER_ACTIVITY_FINISHED = "FINISHED";

    private String mCid;
    private byte[] mAudio;
    private long mOffset;
    private String mPlayerActivity;

    public AvsSpeakItem(String token, String cid, ByteArrayInputStream audio) throws IOException {
        this(token, cid, IOUtils.toByteArray(audio));
        audio.close();
    }

    public AvsSpeakItem(String token, String cid, byte[] audio){
        super(token);
        mCid = cid;
        mAudio = audio;
        mOffset = 0;
        mPlayerActivity = PLAYER_ACTIVITY_FINISHED;
    }

    public String getCid() {
        return mCid;
    }

    public byte[] getAudio() {
        return mAudio;
    }

    public void setOffset(long offset) {
        mOffset = offset;
    }

    public long getOffset() {
        return mOffset;
    }

    public void setPlayerActivity(String activity){  mPlayerActivity = activity; }

    public String getPlayerAcivity(){ return mPlayerActivity; }
}
