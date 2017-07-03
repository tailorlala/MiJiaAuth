package com.xiaomi.smarthome.authlib;

import android.text.TextUtils;
import android.util.Log;

/**
 * Time 2017/4/19.
 * User renlei
 * Email renlei@xiaomi.com
 */

public class AuthLog {
    private static final boolean ENABLE_LOG = BuildConfig.IS_DEBUG_BUILD_TYPE;
    public static void log(String text){
        if (TextUtils.isEmpty(text)){
            return;
        }
        if (ENABLE_LOG){
            Log.e("AuthLog",text);
        }
    }
}
