package com.xiaomi.smarthome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class AuthActivity extends AppCompatActivity {
    Button mAuthDeviceBtn;
    Button mAuthAppBtn;
    TextView mResult;
    ImageView mAppIcon;
    EditText mAppIdET;
    EditText mDeviceET;
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
        IAuthMangerImpl.getInstance().init(AuthActivity.this);///初始化
        mAuthDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE);
            }
        });
        mAuthAppBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_APP);
                                           }
                                       }
        );
    }

    private void onAuthClick(int requestCode){
        Bundle bundle = new Bundle();
        /*if (TextUtils.isEmpty(mAppIdET.getText().toString())){
            Toast.makeText(AuthActivity.this,"extra_application_id 不可以为空",Toast.LENGTH_SHORT);
            return;
        }*/
        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, "9971080915123888");
//        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, mAppIdET.getText().toString());
        if (requestCode == AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE){
            if (TextUtils.isEmpty(mDeviceET.getText().toString())){
                Toast.makeText(AuthActivity.this,"device_id 不可以为空",Toast.LENGTH_SHORT);
                return;
            }
//            bundle.putString(AuthConstants.EXTRA_DEVICE_DID,"58067337");
            bundle.putString(AuthConstants.EXTRA_DEVICE_DID,mDeviceET.getText().toString());
        }
        //发起授权
        IAuthMangerImpl.getInstance().callAuth(AuthActivity.this, bundle, requestCode, new IAuthResponse() {
                    @Override
                    public void onSuccess(int i, final Bundle bundle) {
                        if (bundle != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("结果：").append("\n")
                                    .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                                    .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                                    .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                                    .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID));

                            mResult.setText(sb);
                        }
                    }

                    @Override
                    public void onFail(int i, Bundle bundle) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("结果：").append("\n")
                                .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                                .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                                .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                                .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID));
                        mResult.setText(sb);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IAuthMangerImpl.getInstance().release();
    }
}
