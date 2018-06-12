package com.konka.alexa.alexalib.interfaces.errors;

import com.konka.alexa.alexalib.data.Directive;
import com.konka.alexa.alexalib.interfaces.AvsItem;

/**
 * Created by will on 6/26/2016.
 */

public class AvsResponseException extends AvsItem {
    Directive directive;
    public AvsResponseException(Directive directive) {
        super(null);
        this.directive = directive;
    }

    public Directive getDirective() {
        return directive;
    }
}
