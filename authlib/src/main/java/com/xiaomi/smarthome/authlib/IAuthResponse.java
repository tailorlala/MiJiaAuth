package com.xiaomi.smarthome.authlib;

import android.os.Bundle;

/**
 * Time 2017/4/17.
 * User renlei
 * Email renlei@xiaomi.com
 */

public interface IAuthResponse {
    public void onSuccess(int code,Bundle data);
    public void onFail(int code,Bundle data);
}
