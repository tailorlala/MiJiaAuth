# MiJiaAuth  米家授权

背景：我们开发了一套授权的系统，通过米家App对自己选择的设备进行授权，就可以在其他app中获取这些授权设备的信息并且可以对其进行操作。
前提条件：
在获取授权之前，您需要：
1)	在米家开放平台完成开发者资质认证
2)	完成app应用申请并通过审核，并完善相关资料
3)	下载一个米家App并注册一个属于自己的账号

### 米家Android客户端授权调用

### 1 引入sdk包
compile(name:'authlib-release', ext:"aar")

### 2 在activity的oncreate函数中进行初始化
IAuthMangerImpl.getInstance().init(AuthActivity.this);///初始化

### 3 发起授权IAuthMangerImpl.getInstance().callAuth（final Context context, final Bundle data, final int requestCode, IAuthResponse response）
在授权时，需要将applicationid传入,该id是在开放平台上面进行申请的<br>
 Bundle bundle = new Bundle();<br>
 bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, "xxxxxxxxxxxxx");<br>
 
### 4 接收返回值 通过IAuthResponse
void onSuccess(int code, Bundle data);表示授权成功<br>
void onFail(int code, Bundle data);  表示授权失败<br>
其中code值的含义与下面的对应。  可以直接使用该AuthCode<br>

public class AuthCode {<br>
    public static final int AUTH_SUCCESS = 100;//授权成功<br>
    public static final int PACKAGE_ERROR = -100;//包名错误<br>
    public static final int LACK_PARAMS_ERROR = -101;//缺少参数<br>
    public static final int GET_TOKEN_ERROR = -102;//获取token失败<br>
    public static final int AUTH_ERROR = -103;//授权失败<br>
    public static final int APP_ID_ERROR = -104;//appid有问题<br>
    public static final int APP_SIGN_ERROR = -105;//签名错误<br>

    public static final int REQUEST_CODE_CALL_AUTH = 1;<br>
}<br>
而返回的data中，主要有下面四个值，注意判空使用<br>
 /**返回值*****/<br>
    public static final String EXTRA_TOKEN = "extra_token";<br>
    public static final String EXTRA_RESULT_CODE = "extra_result_code";<br>
    public static final String EXTRA_RESULT_MSG = "extra_result_msg";<br>
    public static final String EXTRA_USER_ID = "extra_user_id";<br>
    
    
public class AuthConstants {<br>
    //    请求参数
    public static final String EXTRA_APPLICATION_ID = "extra_application_id";<br>
    public static final String EXTRA_PACKAGE_NAME = "extra_package_name";<br>
    public static final String EXTRA_APP_SIGN = "extra_app_sign";<br>
    public static final int ACTIVITY_RESULT_FAIL = -2;<br>

    //返回值<br>
    public static final String EXTRA_TOKEN = "extra_token";<br>
    public static final String EXTRA_RESULT_CODE = "extra_result_code";<br>
    public static final String EXTRA_RESULT_MSG = "extra_result_msg";<br>
    public static final String EXTRA_USER_ID = "extra_user_id";<br>
}<br>


具体的可以参考demo
 
 
