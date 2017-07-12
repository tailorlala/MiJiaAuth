package com.xiaomi.smarthome.authlib;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Time 2017/7/7.
 * User renlei
 * Email renlei@xiaomi.com
 */

public class AuthService extends Service {
    private Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthCallBack;
    }

    IAuthCallBack.Stub mAuthCallBack = new IAuthCallBack.Stub() {
        @Override
        public void onSuccess(final int code, final Bundle data) throws RemoteException {
            AuthLog.log("onSuccess" + " code " + code);
            AuthLog.log("   INSTANCE"+AuthManager.getInstance()+"    AuthManager.getInstance().getAuthResponse()" + AuthManager.getInstance().getAuthResponse());
            mHandler = AuthManager.getInstance().getHandler();
            if (mHandler == null) {
                mHandler = new Handler(Looper.getMainLooper());
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (AuthManager.getInstance().getAuthResponse() != null) {
                        AuthLog.log("AuthManager.getInstance().getAuthResponse()  onSuccess" + code);
                        AuthManager.getInstance().getAuthResponse().onSuccess(code, data);
                    }
                }
            });
        }

        @Override
        public void onFail(final int code, final Bundle result) throws RemoteException {
            AuthLog.log("onFail" + " code " + code);
            AuthLog.log("AuthManager.getInstance().getAuthResponse()" + AuthManager.getInstance().getAuthResponse());
            mHandler = AuthManager.getInstance().getHandler();
            if (AuthManager.getInstance().getAuthResponse() != null) {
                if (mHandler == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AuthLog.log("AuthManager.getInstance().getAuthResponse()  onFail" + code);
                        AuthManager.getInstance().getAuthResponse().onFail(code, result);
                    }
                });
            }
        }
    };
}
