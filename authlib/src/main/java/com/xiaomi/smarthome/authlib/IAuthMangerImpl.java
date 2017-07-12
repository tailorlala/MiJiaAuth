package com.xiaomi.smarthome.authlib;

import android.content.Context;
import android.os.Bundle;

/**
 * Time 2017/4/19.
 * User renlei
 * Email renlei@xiaomi.com
 */

public abstract class IAuthMangerImpl {

    public abstract void release();
    public abstract boolean callAuth(final Context context, final Bundle data, final int requestCode, IAuthResponse response);

    /**
     * 给有activity等生命周期时的使用
     * apilevel 4
     * @param context
     * @return
     */
    public abstract int init(Context context);
    public static IAuthMangerImpl getInstance(){
        return AuthManager.getInstance();
    }

    /**
     * 给没有生命周期的初始化，像RN等
     * @apilevel  5
     * @param context
     * @param callBack
     */
    public abstract void intiWithCallBack(Context context,IInitCallBack callBack);
    public abstract int getSdkApiLevel();
}
