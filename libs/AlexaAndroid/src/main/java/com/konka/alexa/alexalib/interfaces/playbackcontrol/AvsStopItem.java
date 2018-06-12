package com.konka.alexa.alexalib.interfaces.playbackcontrol;

import com.konka.alexa.alexalib.interfaces.AvsItem;

/**
 * Directive to stop device playback
 *
 * {@link com.konka.alexa.alexalib.data.Directive} response item type parsed so we can properly
 * deal with the incoming commands from the Alexa server.
 *
 * @author will on 5/21/2016.
 */
public class AvsStopItem extends AvsItem {
    public AvsStopItem(String token) {
        super(token);
    }
}
