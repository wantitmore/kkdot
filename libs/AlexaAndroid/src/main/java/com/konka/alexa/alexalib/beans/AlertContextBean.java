package com.konka.alexa.alexalib.beans;

/**
 * Created by user001 on 2018-4-12.
 */

public class AlertContextBean {

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

    @Override
    public String toString() {
        return "AlertContextBean{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", scheduledTime='" + scheduledTime + '\'' +
                '}';
    }


}
