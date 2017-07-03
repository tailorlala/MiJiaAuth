package com.xiaomi.smarthome.authlib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Time 2017/4/13.
 * User renlei
 * Email renlei@xiaomi.com
 */

public class AuthManager extends IAuthMangerImpl {
    private static final String TAG = "AuthManager";
    private static final String PACKAGE_NAME = "com.xiaomi.smarthome";
    private static final String SERVICE_NAME = "com.xiaomi.smarthome.auth.AuthService";
    private static final String ACTION = "com.xiaomi.smarthome.action.AuthService";
    Context mContext;
    private static volatile AuthManager INSTANCE;
    private IAuthResponse mAuthResponse;
    private ICallAuth mCallAuth;
    private boolean isServiceConn = false;
    private boolean bindSuccess = false;
    private Handler mHandler;
    ExecutorService mExecutor = Executors.newFixedThreadPool(1);
    private IInitCallBack mInitCallBack;

    public AuthManager() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static AuthManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AuthManager.class) {
                INSTANCE = new AuthManager();
            }
        }
        return INSTANCE;
    }

    @Override
    public int init(Context context) {
        this.mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        return initService();
    }

    private int initService() {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        PackageManager packageManager = mContext.getApplicationContext().getPackageManager();
        ResolveInfo info = packageManager.resolveService(intent, 0);
        int bindResult;
        if (info != null) {
            String packageName = info.serviceInfo.packageName;
            String serviceName = info.serviceInfo.name;
            ComponentName componentName = new ComponentName(packageName, serviceName);
            AuthLog.log("packageName：" + packageName + "   serviceName:" + serviceName);
            intent.setComponent(componentName);
            AuthLog.log("getComponentEnabledSetting:" + packageManager.getComponentEnabledSetting(componentName));
            bindSuccess = mContext.getApplicationContext().bindService(intent, connection, BIND_AUTO_CREATE);
            if (bindSuccess) {
                bindResult = 0;
            } else {
                bindResult = -1;
                if (mInitCallBack != null) {
                    mInitCallBack.onServiceConnected(bindResult);
                }
            }
        } else {
            Intent intent2 = new Intent();
            ComponentName componentName = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
            intent2.setComponent(componentName);
            bindSuccess = mContext.getApplicationContext().bindService(intent2, connection, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "bindResult" + bindSuccess);
            if (bindSuccess) {
                bindResult = 0;
            } else {
                bindResult = -2;
            }
        }
        AuthLog.log("bindResult" + bindResult);
        if (bindSuccess && isServiceConn && mInitCallBack != null) {
            mInitCallBack.onServiceConnected(bindResult);
        }
        return bindResult;
    }
    /*private boolean initService() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
        intent.setComponent(componentName);
        bindSuccess = mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindResult" + bindSuccess);
        *//*if (!bindSuccess) {
            Toast.makeText(mContext, "请确认已经安装了米家，并且更新到最新的版本", Toast.LENGTH_SHORT).show();
        }*//*
        return bindSuccess;
    }*/

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AuthLog.log("onServiceConnected");
            mCallAuth = ICallAuth.Stub.asInterface(service);
            isServiceConn = true;
            if (mInitCallBack != null) {
                mInitCallBack.onServiceConnected(0);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AuthLog.log("onServiceDisconnected");
            isServiceConn = false;
            mCallAuth = null;
            if (mInitCallBack != null) {
                mInitCallBack.onServiceDisConnected();
            }
        }
    };

    private int initAuth(Context context) {
        this.mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        return initService();
    }

    @Override
    public boolean callAuth(final Context context, final Bundle data, final int requestCode, IAuthResponse response) {
        this.mContext = context;
        AuthLog.log("mCallAuth " + mCallAuth + "  isServiceConn " + isServiceConn + "  bindSuccess  " + bindSuccess);
        if (mCallAuth == null || !isServiceConn) {
            initAuth(context);
        }
        if (!bindSuccess) {
//            Toast.makeText(mContext, "请确认已经安装了米家，并且更新到最新的版本", Toast.LENGTH_SHORT).show();
            return false;
        }
        mAuthResponse = response;
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Bundle bundle;
                if (data == null) {
                    bundle = new Bundle();
                } else {
                    bundle = data;
                }
                bundle.putString(AuthConstants.EXTRA_APP_SIGN, getAppSignature());
                bundle.putString(AuthConstants.EXTRA_PACKAGE_NAME, context.getPackageName());
                bundle.putInt(AuthConstants.EXTRA_SDK_VERSION_CODE, BuildConfig.VERSION_CODE);
                bundle.putString(AuthConstants.EXTRA_SDK_VERSION_NAME, BuildConfig.VERSION_NAME);
                try {
                    mCallAuth.callAuth(requestCode, bundle, mCallBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private IAuthCallBack mCallBack = new IAuthCallBack.Stub() {
        @Override
        public void onSuccess(final int code, final Bundle data) throws RemoteException {
            AuthLog.log("onSuccess" + " code " + code + " data " + data.toString());
            if (mHandler == null) {
                mHandler = new Handler(Looper.getMainLooper());
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAuthResponse.onSuccess(code, data);
                }
            });
        }

        @Override
        public void onFail(final int code, final Bundle result) throws RemoteException {
            AuthLog.log("onFail" + " code " + code + " data " + result.toString());
            if (mAuthResponse != null) {
                if (mHandler == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAuthResponse.onFail(code, result);
                    }
                });
            }
        }
    };


    private String getAppSignature() {

        try {
            StringBuilder builder = new StringBuilder();
            PackageInfo packageInfo;
            /** 通过包管理器获得指定包名包含签名的包信息 **/
//            Log.d("AuthManager", "应用的包名" + mContext.getPackageName());
            packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            /******* 循环遍历签名数组拼接应用签名 *******/
            for (Signature signature : signatures) {
                builder.append(signature.toCharsString());
            }
            /************** 得到应用签名 **************/
//            Log.d("AuthManager", "应用的签名" + builder.toString());
//            AuthLog.log("签名"+signatures[0].toCharsString());
//            AuthLog.log("签名---"+ getMD5(signatures[0].toCharsString()));
//            AuthLog.log("签名11---"+ encryptionMD5(signatures[0].toByteArray()));
//            Log.d("AuthManager", "应用的签名md5" + getMD5(builder.toString()));
//            return encryptionMD5(signatures[0].toByteArray());
            return encryptionMD5(signatures[0].toByteArray()).toUpperCase();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /***
     * MD5加码 生成32位md5码
     */
    private String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
        }
    }

    private String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }


    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    @Override
    public void release() {
        try {
            AuthLog.log("isServiceConn" + isServiceConn);
            if (bindSuccess && connection != null && mContext != null && isServiceConn) {
                mContext.getApplicationContext().unbindService(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void intiWithCallBack(Context context, IInitCallBack callBack) {
        this.mContext = context;
        this.mInitCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
        initService();
    }

    @Override
    public int getSdkApiLevel() {
        return BuildConfig.VERSION_CODE;
    }
}
