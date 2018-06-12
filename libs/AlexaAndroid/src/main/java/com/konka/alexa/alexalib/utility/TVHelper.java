package com.konka.alexa.alexalib.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;

import java.util.List;

/**
 * Created by stone on 17-7-7.
 */

public class TVHelper {
    private static final String TAG = "[TVHelper]";

    public static final String DTV = "DTV";
    public static final String ATV = "ATV";
    public static final String AV = "AV";
    public static final String YPBPR = "YPBPR";
    public static final String VGA = "VGA";
    public static final String HDMI1 = "HDMI 1";
    public static final String HDMI2 = "HDMI 2";
    public static final String HDMI3 = "HDMI 3";

    public static final String CMD_LAST_CHANNEL = "LAST CHANNEL";
    public static final String CMD_NEXT_CHANNEL = "NEXT CHANNEL";
    public static final String CMD_PREVIOUS_CHANNEL = "PREVIOUS CHANNEL";
    public static final String CMD_FIRST_CHANNEL = "FIRST CHANNEL";

    private static String[] mSourceList = new String[]{DTV, ATV, AV, YPBPR, VGA, HDMI1, HDMI2, HDMI3};

    // tv special function
    public static final String EPG = "EPG";
    public static final String PVR = "PVR";
    public static final String INFO = "INFO";

    // tv special function keycode
    public static final int KEYCODE_EPG = 0x0116;
    public static final int KEYCODE_PVR = 299;
    public static final int KEYCODE_INFO = 0x00A5;

    public static boolean isSourceValidate(String source){
        for (String item : mSourceList){
            if(item.equals(source)){
                return true;
            }
        }
        return false;
    }

    /**
     * get current input source
     * @return
     */
    public static int getCurrentInputSource(){
        return TvCommonManager.getInstance().getCurrentTvInputSource();
    }

    /**
     * set input source
     * @param inputSource
     */
    public static void setInputSource(int inputSource){
        TvCommonManager.getInstance().setInputSource(inputSource);
    }

    public static void setInputSourceThroughTvPlayer(Context context, int inputSource){
        Log.v(TAG , " input source to:" + inputSource);
        Intent intent = new Intent("com.konka.leanback.trunk.intent.action.TvActivity");
        intent.putExtra("LauncherNewSrc", inputSource);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startEPG(Context context){
        Intent intent = new Intent("com.konka.leanback.intent.action.EpgActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startTVSpecialFunc(Context context, int keycode){
        Intent intent = new Intent("com.konka.quickmanu.action.KEYDOWN");
        intent.putExtra("keyCode", keycode);
        intent.putExtra("keyevent", KeyEvent.ACTION_DOWN);
        context.sendBroadcast(intent);
    }

    public static boolean isSpecialInputSource(int source){
        return getCurrentInputSource() == source;
    }

    /**
     * current input source is STORAGE or not
     * @return
     */
    public static boolean isStorageInputSource(){
        return isSpecialInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
    }

    /**
     * current input source is DTV or not
     * @return
     */
    public static boolean isDTVInputSource(){
        return isSpecialInputSource(TvCommonManager.INPUT_SOURCE_DTV);
    }

    /**
     * switch channel with command 'next channel'
     * @return
     */
    public static boolean channelUp(){
        return TvChannelManager.getInstance().programUp();
    }

    /**
     * switch channel with command 'previous channel'
     * @return
     */
    public static boolean channelDown(){
        return TvChannelManager.getInstance().programDown();
    }

    public static boolean toSpecialChannel(int channelNo, int serviceType){
        // maybe always return false
        Log.d(TAG, "toSpecialChannel: channel is " + channelNo);
        TvChannelManager.getInstance().selectProgram(channelNo, serviceType);
        return true;
    }

    /**
     * switch channel with channel number
     * @param majorNum
     * @return
     */
    public static boolean toDTVSpecialChannel(int majorNum){
        return toSpecialChannel(majorNum, TvChannelManager.SERVICE_TYPE_DTV);
    }

    public static ProgramInfo getProgramInfo(String channelName){
        ProgramInfo programInfo = null;
        int serviceNum = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        List<ProgramInfo> programInfoList = TvChannelManager.getInstance().getProgramInfolist(serviceNum);
        channelName = channelName.toUpperCase();
        for (ProgramInfo item : programInfoList){
            // VCLog.v("channel name:" + item.serviceName);
            if (item.serviceName.toUpperCase().contains(channelName)){
                programInfo = item;
                break;
            }
        }
        return programInfo;
    }

    public static boolean isProgramExist(String channelName){
        int serviceNum = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        List<ProgramInfo> programInfoList = TvChannelManager.getInstance().getProgramInfolist(serviceNum);
        channelName = channelName.toUpperCase();
        for (ProgramInfo item : programInfoList){
            if (item.serviceName.toUpperCase().contains(channelName)){
                return true;
            }
        }
        return false;
    }

    public static boolean isAValidChannelNo(int channelNo){
        int serviceNum = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        List<ProgramInfo> programInfoList = TvChannelManager.getInstance().getProgramInfolist(serviceNum);
        for (ProgramInfo item : programInfoList){
            if (item.majorNum == channelNo){
                return true;
            }
        }
        return false;
    }

//    /**
//     * switch channel with channel name
//     * @param channelName
//     * @return
//     */
    public static boolean toDTVSpecialChannel(String channelName){
        if (Tools.isAInteger(channelName)){
            int channelNo = Integer.parseInt(channelName);
            Log.d(TAG, "toDTVSpecialChannel: channel is " + channelNo);
            if (isAValidChannelNo(channelNo)){
                return toDTVSpecialChannel(channelNo);
            }
        }
        else {
            ProgramInfo programInfo = getProgramInfo(channelName);
            if (programInfo == null){
                Log.v(TAG, "can not find the channel:" + channelName);
                return false;
            }

            Log.v(TAG,"to dtv special channel service id:" + programInfo.majorNum);
            return toDTVSpecialChannel(programInfo.majorNum);
        }
        return false;
    }

    public static boolean toLastChannel(){
        boolean ret = true;
        int programCount = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        if (programCount == 0){
            return false;
        }
        // ProgramInfo programInfo = TvChannelManager.getInstance().getProgramInfoByIndex(programCount);
        ProgramInfo programInfo = TvChannelManager.getInstance().getProgramInfolist(programCount).get(programCount - 1);
        toDTVSpecialChannel(programInfo.majorNum);
        return ret;
    }

    public static boolean toFirstChannel(){
        boolean ret = true;
        int programCount = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        if (programCount == 0){
            return false;
        }
        // ProgramInfo programInfo = TvChannelManager.getInstance().getProgramInfoByIndex(0);
        ProgramInfo programInfo = TvChannelManager.getInstance().getProgramInfolist(programCount).get(0);
        // VCLog.v("to first channel:" + programCount + " service id:" + programInfo.majorNum);
        toDTVSpecialChannel(programInfo.majorNum);
        return ret;
    }

    public static boolean toNextChannel(){
        boolean ret = true;
        channelUp();
        return ret;
    }

    public static boolean toPreviousChannel(){
        boolean ret = true;
        channelDown();
        return ret;
    }
}
