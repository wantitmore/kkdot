package com.willblaschko.android.alexa.beans;

/**
 * Created by user001 on 2018-4-13.
 */

public class TestAlertContextBean {
    private String token;
    private String type;
    private String scheduledTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
