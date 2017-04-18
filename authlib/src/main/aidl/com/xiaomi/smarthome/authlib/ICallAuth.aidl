// callAuth.aidl
package com.xiaomi.smarthome.authlib;

// Declare any non-default types here with import statements
import com.xiaomi.smarthome.authlib.IAuthCallBack;
interface ICallAuth {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void callAuth(int requestCode,in Bundle data,IAuthCallBack callBack);
}
