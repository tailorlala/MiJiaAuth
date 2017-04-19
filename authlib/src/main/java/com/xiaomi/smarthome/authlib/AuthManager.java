package com.xiaomi.smarthome.authlib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Time 2017/4/13.
 * User renlei
 * Email renlei@xiaomi.com
 */

public class AuthManager extends IAuthMangerImpl{
    private static final String TAG = "AuthManager";
    private static final String PACKAGE_NAME = "com.xiaomi.smarthome";
    private static final String SERVICE_NAME = "com.xiaomi.smarthome.auth.AuthService";
    Context mContext;
    private static volatile AuthManager INSTANCE;
    private IAuthResponse mAuthResponse;
    private ICallAuth mCallAuth;
    private boolean isServiceConn = false;
    private boolean bindSuccess = false;
    private Handler mHandler;
    ExecutorService mExecutor = Executors.newFixedThreadPool(1);

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
    public boolean init(Context context) {
        this.mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        return initService();
    }

    private boolean initService() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
        intent.setComponent(componentName);
        bindSuccess = mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindResult" + bindSuccess);
        if (!bindSuccess) {
            Toast.makeText(mContext, R.string.bind_failed_msg, Toast.LENGTH_SHORT).show();
        }
        return bindSuccess;
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AuthLog.log("onServiceConnected");
            mCallAuth = ICallAuth.Stub.asInterface(service);
            isServiceConn = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AuthLog.log("onServiceDisconnected");
            isServiceConn = false;
        }
    };

    public boolean initAuth(Context context) {
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
            Toast.makeText(mContext, R.string.bind_failed_msg, Toast.LENGTH_SHORT).show();
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

    public boolean callAuthnewold(Activity context, Bundle data, int requestCode) {
        this.mContext = context;
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.smarthome.action.auth");
        intent.putExtra(AuthConstants.EXTRA_APPLICATION_ID, "44212592383956891");
        intent.putExtra(AuthConstants.EXTRA_APP_SIGN, getAppSignature());
        intent.putExtra(AuthConstants.EXTRA_PACKAGE_NAME, context.getPackageName());
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            // 说明系统中不存在这个activity
            Toast.makeText(mContext, "其确认已经安装了米家App", Toast.LENGTH_SHORT).show();
            return false;
        }
        context.startActivityForResult(intent, requestCode);
        return true;
    }

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
            return builder.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public void release() {
        if (bindSuccess && connection != null && mContext != null) {
            mContext.unbindService(connection);
        }
    }

}
