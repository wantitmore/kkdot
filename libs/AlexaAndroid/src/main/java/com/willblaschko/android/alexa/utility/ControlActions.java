package com.willblaschko.android.alexa.utility;

/**
 * Created by stone on 17-6-22.
 */

public class ControlActions {

    /**
     * no actions
     */
    public static final int ACTION_NONE = 0;

    /**
     * TV action: to open a application
     */
    public static final int ACTION_OPEN_APP = 1;

    /**
     * TV action: to uninstall a application
     */
    public static final int ACTION_UNINSTALL_APP = 2;

    /**
     * TV action: to search and install a application
     * currently use Zeason TV store resource
     */
    public static final int ACTION_SCEARCH_INSTALL_APP = 3;

    /**
     * TV action: to change input source
     */
    public static final int ACTION_CHANGE_INPUT_SOURCE = 4;

    /**
     * TV action: to search online video
     * currently use Zeason video tv resource
     */
    public static final int ACTION_SEARCH_ONLINE_VIDEO = 5;

    /**
     * TV action: to open a settings item
     */
    public static final int ACTION_TV_SETTINGS = 6;

    /**
     * TV action: to change channel
     * next channel / previous channel or just channel name
     */
    public static final int ACTION_CHANGE_CHANNEL = 7;

    /**
     * TV action: to open TV special function
     * like: EPG / PVR and so on
     */
    public static final int ACTION_TV_SPECIAL_FUNC = 8;


    // Android TV main parts command
    /**
     * TV main action: open home
     */
    public static final int ACTION_OPEN_HOME = 101;

    /**
     * TV main action: open tv
     */
    public static final int ACTION_OPEN_TV = 102;

    /**
     * TV main action: open settings
     */
    public static final int ACTION_OPEN_SETTINGS = 103;

    /**
     * TV main action: setup network
     */
    public static final int ACTION_SETUP_NETWORK = 104;

    /**
     * TV main action: set date and time
     */
    public static final int ACTION_SET_DATE_AND_TIME = 105;

    /**
     * TV main action: setup language
     */
    public static final int ACTION_SETUP_LANGUAGE = 106;

    /**
     * TV main action: setup bluetooth
     */
    public static final int ACTION_SETUP_BLUETOOTH = 107;

    public static final int ACTION_VOLUME_UP = 108;

    public static final int ACTION_VOLUME_DOWN = 109;

    public static final int ACTION_MUTE = 110;

    public static final int ACTION_UNMUTE = 111;


    // TV actions
    public static final int ACTION_OPEN_EPG = 300;

    public static final int ACTION_OPEN_PVR = 301;

    public static final int ACTION_SHOW_INFO = 302;

    public static final int ACTION_SHOW_HELP = 303;

}
