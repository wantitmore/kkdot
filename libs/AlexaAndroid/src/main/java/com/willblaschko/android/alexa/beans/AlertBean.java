package com.willblaschko.android.alexa.beans;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user001 on 2018-4-3.
 */

public class AlertBean extends DataSupport {

    private int id;
    private String type;
    private String scheduledTime;
    private long loopPauseInMilliSeconds;
    private long loopCount;
    private String token;
    private String backgroundAlertAsset;
    private List<AssetsBean> assets;
    private List<String> assetPlayOrder;
    private List<String> assetIds;
    private List<String> assetUrls;
    private boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public long getLoopPauseInMilliSeconds() {
        return loopPauseInMilliSeconds;
    }

    public void setLoopPauseInMilliSeconds(long loopPauseInMilliSeconds) {
        this.loopPauseInMilliSeconds = loopPauseInMilliSeconds;
    }

    public void setLoopCount(long loopCount) {
        this.loopCount = loopCount;
    }

    public long getLoopCount() {
        return loopCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBackgroundAlertAsset() {
        return backgroundAlertAsset;
    }

    public void setBackgroundAlertAsset(String backgroundAlertAsset) {
        this.backgroundAlertAsset = backgroundAlertAsset;
    }

    public List<AssetsBean> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsBean> assets) {
        this.assets = assets;
    }

    public List<String> getAssetPlayOrder() {
        return assetPlayOrder;
    }

    public void setAssetPlayOrder(List<String> assetPlayOrder) {
        this.assetPlayOrder = assetPlayOrder;
    }

    public static class AssetsBean implements Serializable{
        /**
         * assetId : 3af1a946-40e5-3413-bc7b-085f8d220799
         * url : https://s3.amazonaws.com/deeappservice.prod.notificationtones/system_alerts_melodic_01.mp3
         */

        private String assetId;
        private String url;

        public String getAssetId() {
            return assetId;
        }

        public void setAssetId(String assetId) {
            this.assetId = assetId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public List<String> getAssetIds() {
        return assetIds;
    }

    public void setAssetIds(List<String> assetIds) {
        this.assetIds = assetIds;
    }

    public List<String> getAssetUrls() {
        return assetUrls;
    }

    public void setAssetUrls(List<String> assetUrls) {
        this.assetUrls = assetUrls;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
