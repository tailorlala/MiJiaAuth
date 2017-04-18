// IAuthCallBack.aidl
package com.xiaomi.smarthome.authlib;

// Declare any non-default types here with import statements

interface IAuthCallBack {
    void onSuccess(int code,inout Bundle data);
    void onFail(int code,inout Bundle result);

}
