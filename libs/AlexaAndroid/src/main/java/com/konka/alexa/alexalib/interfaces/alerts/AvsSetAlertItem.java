package com.konka.alexa.alexalib.interfaces.alerts;

import android.util.Log;

import com.konka.alexa.alexalib.beans.AlertBean;
import com.konka.alexa.alexalib.data.Directive;
import com.konka.alexa.alexalib.interfaces.AvsItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * An AVS Item to handle setting alerts on the device
 *
 * {@link com.konka.alexa.alexalib.data.Directive} response item type parsed so we can properly
 * deal with the incoming commands from the Alexa server.
 */
public class AvsSetAlertItem extends AvsItem{
    private Directive directive;
    String token;
    private String type;
    private String scheduledTime;
    private List<AlertBean.AssetsBean> assets;
    private List<String> assetPlayOrder;
    private long loopPauseInMilliSeconds;
    private long loopCount;
    private String backgroundAlertAsset;

    public static final String TIMER = "TIMER";
    public static final String ALARM = "ALARM";
    public static final String REMINDER = "REMINDER";
    private final Directive.Payload payload;

    /**
     * SetAlertItem info
     * @param token
     * @param directive
     */
    public AvsSetAlertItem(String token, Directive directive){
        super(token);
        Log.d("AvsSetAlertItem", "AvsSetAlertItem: ");
        this.token = token;
        this.directive = directive;
        payload = directive.getPayload();
    }

    public Directive.Payload getPayload() {
        return payload;
    }

    public String getScheduledTime() {
        return payload.getScheduledTime();
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public long getScheduledTimeMillis() throws ParseException {
        return getDate().getTime();
    }

    public int getHour() throws ParseException {
        return getDate().getHours();
    }
    public int getMinutes() throws ParseException {
        return getDate().getMinutes();
    }

    public Date getDate() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(payload.getScheduledTime());
    }

    public String getType() {
        return payload.getType();
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTimer() {
        return payload.getType().equals(TIMER);
    }

    public boolean isAlarm() {
        return payload.getType().equals(ALARM);
    }

    public boolean isReminder() {
        return payload.getType().equals(REMINDER);
    }

    public List<AlertBean.AssetsBean> getAssets() {
        return payload.getAssets();
    }

    public void setAssets(List<AlertBean.AssetsBean> assets) {
        this.assets = assets;
    }

    public List<String> getAssetPlayOrder() {
        return payload.getAssetPlayOrder();
    }

    public void setAssetPlayOrder(List<String> assetPlayOrder) {
        this.assetPlayOrder = assetPlayOrder;
    }

    public long getLoopPauseInMilliSeconds() {
        return payload.getLoopPauseInMilliSeconds();
    }

    public void setLoopPauseInMilliSeconds(long loopPauseInMilliSeconds) {
        this.loopPauseInMilliSeconds = loopPauseInMilliSeconds;
    }

    public long getLoopCount() {
        return payload.getLoopCount();
    }

    public void setLoopCount(long loopCount) {
        this.loopCount = loopCount;
    }

    public String getBackgroundAlertAsset() {
        return payload.getBackgroundAlertAsset();
    }

    public void setBackgroundAlertAsset(String backgroundAlertAsset) {
        this.backgroundAlertAsset = backgroundAlertAsset;
    }


}
