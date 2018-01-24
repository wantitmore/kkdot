package com.willblaschko.android.alexa.utility;

/**
 * Created by stone on 17-9-11.
 */

public class Tools {

    public static boolean isAInteger(String input){
        try {
            Integer.parseInt(input);
        }
        catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
