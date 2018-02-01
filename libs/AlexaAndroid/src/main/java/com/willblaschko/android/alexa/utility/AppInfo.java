package com.willblaschko.android.alexa.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stone on 17-6-28.
 */

public class AppInfo implements Parcelable{

    private String mAppName = null;
    private String mPackageName = null;

    public AppInfo(String appName, String packageName){
        this.mAppName = appName;
        this.mPackageName = packageName;
    }

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAppName);
        dest.writeString(mPackageName);
    }

    public static Creator<AppInfo> CREATOR = new Creator<AppInfo>(){

        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public AppInfo(Parcel source){
        mAppName = source.readString();
        mPackageName = source.readString();
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "mAppName='" + mAppName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                '}';
    }
}
