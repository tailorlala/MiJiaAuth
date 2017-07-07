package com.xiaomi.smarthome.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiaomi.smarthome.authlib.IAuthCallBack;

/**
 * Time 2017/7/7.
 * User renlei
 * Email renlei@xiaomi.com
 */

public class AuthCallBackInfo implements Parcelable {
    public IAuthCallBack mAuthCallBack;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (mAuthCallBack != null){
            dest.writeInt(1);
            dest.writeStrongBinder(mAuthCallBack.asBinder());
        }
    }

    public AuthCallBackInfo() {
    }

    protected AuthCallBackInfo(Parcel in) {
        int hasBinder = in.readInt();
        if (hasBinder == 1){
            this.mAuthCallBack = IAuthCallBack.Stub.asInterface(in.readStrongBinder());
        }
    }

    public static final Parcelable.Creator<AuthCallBackInfo> CREATOR = new Parcelable.Creator<AuthCallBackInfo>() {
        @Override
        public AuthCallBackInfo createFromParcel(Parcel source) {
            return new AuthCallBackInfo(source);
        }

        @Override
        public AuthCallBackInfo[] newArray(int size) {
            return new AuthCallBackInfo[size];
        }
    };
}
