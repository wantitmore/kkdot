package com.willblaschko.android.alexa.utility;

import android.os.Parcel;
import android.os.Parcelable;

import com.amazon.identity.auth.device.dataobject.AppInfo;

import java.util.ArrayList;

/**
 * Created by stone on 17-6-28.
 */

public class ActionResult implements Parcelable{
    private int mActionType = ControlActions.ACTION_NONE;
    private String mActionMessage = null;

    // extend info
    private ArrayList<AppInfo> mAppInfoList = null;

    public ActionResult(){
        this(ControlActions.ACTION_NONE, "");
    }

    public ActionResult(int actionType){
        this(actionType, "");
    }

    public ActionResult(int actionType, String actionMessage){
        this.mActionType = actionType;
        this.mActionMessage = actionMessage;
    }

    public ActionResult(Parcel in){
        mActionType = in.readInt();
        mActionMessage = in.readString();
    }

    public int getmActionType() {
        return mActionType;
    }

    public void setmActionType(int mActionType) {
        this.mActionType = mActionType;
    }

    public String getmActionMessage() {
        return mActionMessage;
    }

    public void setmActionMessage(String mActionMessage) {
        this.mActionMessage = mActionMessage;
    }

    public ArrayList<AppInfo> getmAppInfoList() {
        return mAppInfoList;
    }

    public void setmAppInfoList(ArrayList<AppInfo> appInfoList) {
        this.mAppInfoList = appInfoList;
    }

    @Override
    public String toString() {
        return "ActionResult{" +
                "mActionType=" + mActionType +
                ", mActionMessage='" + mActionMessage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mActionType);
        dest.writeString(mActionMessage);
    }

    public static Creator<ActionResult> CREATOR = new Creator<ActionResult>(){

        @Override
        public ActionResult createFromParcel(Parcel source) {
            return new ActionResult(source);
        }

        @Override
        public ActionResult[] newArray(int size) {
            return new ActionResult[size];
        }
    };
}
