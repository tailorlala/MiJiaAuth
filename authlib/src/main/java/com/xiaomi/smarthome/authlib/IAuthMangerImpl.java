package com.xiaomi.smarthome.authlib;

import android.content.Context;
import android.os.Bundle;

/**
 * Time 2017/4/19.
 * User renlei
 * Email renlei@xiaomi.com
 */

public abstract class IAuthMangerImpl {
    protected static IAuthMangerImpl INSTANCE = null;
    /*AuthManager mAuthManager = AuthManager.getInstance();
    private static IAuthMangerImpl INSTANCE;
    public static IAuthMangerImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (AuthManager.class) {
                INSTANCE = new IAuthMangerImpl();
            }
        }
        return INSTANCE;
    }

    public void release() {
        mAuthManager.release();
    }

    public boolean init(Context context) {
       return mAuthManager.initAuth(context);
    }

    public boolean callAuth(final Context context, final Bundle data, final int requestCode, IAuthResponse response) {
        return mAuthManager.callAuth(context,data,requestCode,response);
    }*/
    public abstract void release();
    public abstract boolean callAuth(final Context context, final Bundle data, final int requestCode, IAuthResponse response);
    public abstract boolean init(Context context);
    public static IAuthMangerImpl getInstance(){
        if (INSTANCE == null) {
            synchronized (IAuthMangerImpl.class) {
                INSTANCE = new AuthManager();
            }
        }
        return INSTANCE;
    };
}
