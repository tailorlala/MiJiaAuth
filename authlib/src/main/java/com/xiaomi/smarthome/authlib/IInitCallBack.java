package com.xiaomi.smarthome.authlib;

/**
 * Time 2017/6/30.
 * User renlei
 * Email renlei@xiaomi.com
 */

public abstract class IInitCallBack {
    public abstract void onServiceConnected(int result);

    public  void onServiceDisConnected(){};
}
