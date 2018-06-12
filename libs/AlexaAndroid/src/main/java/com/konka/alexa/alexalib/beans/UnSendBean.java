package com.konka.alexa.alexalib.beans;

import org.litepal.crud.DataSupport;

/**
 * Created by user001 on 2018-4-17.
 */

public class UnSendBean extends DataSupport {

    private String token;
    private String type;
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
}