package com.xiaomi.smarthome.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.smarthome.authlib.AuthCode;
import com.xiaomi.smarthome.authlib.AuthConstants;
import com.xiaomi.smarthome.authlib.IAuthMangerImpl;
import com.xiaomi.smarthome.authlib.IAuthResponse;
import com.xiaomi.smarthome.authlib.IInitCallBack;

public class AuthActivity extends Activity {
    Button mAuthDeviceBtn;
    Button mAuthAppBtn;
    TextView mResult;
    ImageView mAppIcon;
    EditText mAppIdET;
    EditText mDeviceET;
    Button mRelase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuthDeviceBtn = (Button) findViewById(R.id.go_auth);
        mAuthAppBtn = (Button) findViewById(R.id.go_auth2);
        mResult = (TextView) findViewById(R.id.result);
        mAppIcon = (ImageView) findViewById(R.id.app_icon);
        mAppIdET = (EditText) findViewById(R.id.app_id);
        mDeviceET = (EditText) findViewById(R.id.device);
        mRelase = (Button) findViewById(R.id.release_btn);
        mAuthDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IAuthMangerImpl
                IAuthMangerImpl.getInstance().intiWithCallBack(AuthActivity.this, new IInitCallBack() {
                    @Override
                    public void onServiceConnected(int result) {
                        Toast.makeText(AuthActivity.this, "已经初始化完毕啦", Toast.LENGTH_SHORT).show();
                        Log.d("AuthActivity", "IAuthMangerImpl.getInstance().getSdkApiLevel()" + IAuthMangerImpl.getInstance().getSdkApiLevel() + "   result:    " + result);
//                        onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE);
//                        goToDeviceAuth(AuthActivity.this,"9971080915123888","58067422");///设备授权
                        goToBindDeviceAuth(AuthActivity.this,"9971080915123888","58067422","bindKey");///需要绑定设备的授权

                    }

                });
            }
        });
        mRelase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAuthMangerImpl.getInstance().release();
            }
        });
    }

    /**
     * 发起设备授权，该设备已经在米家app绑定过的
     *
     * @param context  最好是activity的
     * @param appId
     * @param deviceId
     */
    private void goToDeviceAuth(Context context, String appId, String deviceId) {
        Bundle bundle = new Bundle();
        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, appId);
        bundle.putString(AuthConstants.EXTRA_DEVICE_DID, deviceId);
        //发起授权
        IAuthMangerImpl.getInstance().callAuth(context, bundle, AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE, new IAuthResponse() {
                    @Override
                    public void onSuccess(int i, final Bundle bundle) {
                        print(bundle);
                    }

                    @Override
                    public void onFail(int i, Bundle bundle) {
                        print(bundle);
                    }
                }
        );
    }

    /**
     * 发起设备绑定授权，该设备没有在米家app绑定过，需要走绑定流程
     *
     * @param context  最好是activity的
     * @param appId
     * @param deviceId
     */
    private void goToBindDeviceAuth(Context context, String appId, String deviceId, String bindKey) {
        Bundle bundle = new Bundle();
        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, appId);
        bundle.putString(AuthConstants.EXTRA_DEVICE_DID, deviceId);
        bundle.putString(AuthConstants.EXTRA_DEVICE_BIND_KEY, bindKey);
        //发起授权
        IAuthMangerImpl.getInstance().callAuth(context, bundle, AuthCode.REQUEST_CODE_CALL_AUTH_FOR_BIND_DEVICE, new IAuthResponse() {
                    @Override
                    public void onSuccess(int i, final Bundle bundle) {
                        print(bundle);
                    }

                    @Override
                    public void onFail(int i, Bundle bundle) {
                        print(bundle);
                    }
                }
        );
    }


    private void print(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        if (bundle == null)
            return;
        sb.append("结果：").append("\n")
                .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID));
        Log.d("result", sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IAuthMangerImpl.getInstance().release();
    }
}
