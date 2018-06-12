package com.konka.alexa.alexalib.interfaces.playbackcontrol;

import com.konka.alexa.alexalib.interfaces.AvsItem;

/**
 * {@link com.konka.alexa.alexalib.data.Directive} to send a pause command to any app playing media
 *
 * This directive doesn't seem applicable to mobile applications
 *
 * @author will on 5/31/2016.
 */

public class AvsMediaPauseCommandItem extends AvsItem {
    public AvsMediaPauseCommandItem(String token) {
        super(token);
    }
}
