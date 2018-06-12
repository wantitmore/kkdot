package com.konka.alexa.alexalib.interfaces;

import java.util.ArrayList;

/**
 * Wrapper for the list of {@link AvsItem} {@link com.konka.alexa.alexalib.data.Directive}s returned from a post/get sent to the
 * Alexa server. In the future this will contain other metadata associated with the returned response.
 */
public class AvsResponse extends ArrayList<AvsItem> {

    public AvsResponse() {

    }
    
}
