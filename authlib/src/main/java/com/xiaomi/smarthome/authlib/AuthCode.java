package com.xiaomi.smarthome.authlib;

/**
 * Time 2017/4/17.
 * User renlei
 * Email renlei@xiaomi.com
 */

public class AuthCode {
    public static final int AUTH_SUCCESS = 100;//授权成功
    public static final int PACKAGE_ERROR = -100;//包名错误
    public static final int LACK_PARAMS_ERROR = -101;//缺少参数
    public static final int GET_TOKEN_ERROR = -102;//获取token失败
    public static final int AUTH_ERROR = -103;//授权失败
    public static final int APP_ID_ERROR = -104;//appid有问题
    public static final int APP_SIGN_ERROR = -105;//签名错误
    public static final int AUTH_CANCEL = -106;///取消授权
    public static final int REQUEST_AUTH_ERROR = -107;//请求授权失败
    public static final int REQUEST_CODE_ERROR = -108;//请求的code错误
    public static final int REQUSET_DID_ERROR = -109;///缺少did
    public static final int REQUEST_AUTH_NO_CAPABILITY = -110;///该设备不支持语音授权，或者该设备不属于你的名下
    public static final int REQUEST_AUTH_NO_PERMISSION = -111;///该账号不支持该类型授权，请到开放平台申请


    public static final int REQUEST_CODE_CALL_AUTH_FOR_APP = 4;//给应用授权
    public static final int REQUEST_CODE_CALL_AUTH_FOR_DEVICE = 2;//给设备授权
    public static final int REQUEST_CODE_CALL_AUTH_FOR_BIND_DEVICE = 6;//给需要绑定的设备授权
}
