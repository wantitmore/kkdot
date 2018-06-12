package com.konka.alexa.alexalib.requestbody;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * An implemented class that automatically fills in the required MediaType for the {@link RequestBody} that is sent
 * in the {@link com.konka.alexa.alexalib.interfaces.SendEvent} class.
 *
 * @author will on 5/28/2016.
 */
public abstract class DataRequestBody extends RequestBody {
    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }
}
