package com.xiaomi.smarthome.auth;

import android.app.Activity;
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
        /*int isResult =  IAuthMangerImpl.getInstance().init(AuthActivity.this);///初始化
        if (isResult != 0){
            Toast.makeText(AuthActivity.this, "请确认已经安装了米家，并且更新到最新的版本啦", Toast.LENGTH_SHORT).show();
        }*/

        mAuthDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IAuthMangerImpl
                if (IAuthMangerImpl.getInstance().getSdkApiLevel() >= 4) {
                    IAuthMangerImpl.getInstance().intiWithCallBack(AuthActivity.this, new IInitCallBack() {
                        @Override
                        public void onServiceConnected(int result) {
//                        Toast.makeText(AuthActivity.this, "已经初始化完毕啦", Toast.LENGTH_SHORT).show();
                            Log.d("AuthActivity","IAuthMangerImpl.getInstance().getSdkApiLevel()" + IAuthMangerImpl.getInstance().getSdkApiLevel()+"   result:    "+result);
                            onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE);
                        }

                    });
                }
            }
        });
        mAuthAppBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               onAuthClick(AuthCode.REQUEST_CODE_CALL_AUTH_FOR_APP);
                                           }
                                       }
        );

        mRelase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAuthMangerImpl.getInstance().release();
            }
        });
    }

    private void onAuthClick(int requestCode) {
        Bundle bundle = new Bundle();
        /*if (TextUtils.isEmpty(mAppIdET.getText().toString())){
            Toast.makeText(AuthActivity.this,"extra_application_id 不可以为空",Toast.LENGTH_SHORT);
            return;
        }*/
        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, "9971080915123888");
//        bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, mAppIdET.getText().toString());
        if (requestCode == AuthCode.REQUEST_CODE_CALL_AUTH_FOR_DEVICE) {
            if (TextUtils.isEmpty(mDeviceET.getText().toString())) {
                Toast.makeText(AuthActivity.this, "device_id 不可以为空", Toast.LENGTH_SHORT);
                return;
            }
//            bundle.putString(AuthConstants.EXTRA_DEVICE_DID,"58067337");
            bundle.putString(AuthConstants.EXTRA_DEVICE_DID, mDeviceET.getText().toString());
        }
        //发起授权
        boolean isSuccess = IAuthMangerImpl.getInstance().callAuth(AuthActivity.this, bundle, requestCode, new IAuthResponse() {
                    @Override
                    public void onSuccess(int i, final Bundle bundle) {
                        if (bundle != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("结果：").append("\n")
                                    .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                                    .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                                    .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                                    .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID)).append("\n")
                                    .append("versionInfo:").append(bundle.getString(AuthConstants.EXTRA_VERSION_INFO));

                            mResult.setText(sb);
                        }
                    }

                    @Override
                    public void onFail(int i, Bundle bundle) {
                        StringBuilder sb = new StringBuilder();
                        if (bundle == null)
                            return;
                        sb.append("结果：").append("\n")
                                .append("resultCode：").append(bundle.getInt(AuthConstants.EXTRA_RESULT_CODE, -1)).append("\n")
                                .append("resultMsg：").append(bundle.getString(AuthConstants.EXTRA_RESULT_MSG, "")).append("\n")
                                .append("token：").append(bundle.getString(AuthConstants.EXTRA_TOKEN, "")).append("\n")
                                .append("user_id").append(bundle.getString(AuthConstants.EXTRA_USER_ID));
                        mResult.setText(sb);
                    }
                }
        );
        if (!isSuccess) {
            Toast.makeText(AuthActivity.this, "请确认已经安装了米家，并且更新到最新的版本啦.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IAuthMangerImpl.getInstance().release();
    }
}
