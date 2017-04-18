package com.xiaomi.smarthome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomi.smarthome.authlib.AuthCode;
import com.xiaomi.smarthome.authlib.AuthConstants;
import com.xiaomi.smarthome.authlib.AuthManager;
import com.xiaomi.smarthome.authlib.IAuthResponse;

public class AuthActivity extends AppCompatActivity {
    Button mAuthBtn;
    Button mAuthBtn2;
    TextView mResult;
    ImageView mAppIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuthBtn = (Button) findViewById(R.id.go_auth);
        mAuthBtn2 = (Button) findViewById(R.id.go_auth2);
        mResult = (TextView) findViewById(R.id.result);
        mAppIcon = (ImageView) findViewById(R.id.app_icon);

        mAuthBtn.setVisibility(View.GONE);
        AuthManager.getInstance().initAuth(AuthActivity.this);///初始化
        mAuthBtn2.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Bundle bundle = new Bundle();
                                             bundle.putString(AuthConstants.EXTRA_APPLICATION_ID, "44212592383956891");
                                             AuthManager.getInstance().callAuth(AuthActivity.this, bundle, AuthCode.REQUEST_CODE_CALL_AUTH, new IAuthResponse() {
                                                         @Override
                                                         public void onSuccess(int i, final Bundle bundle) {
                                                             if (bundle != null) {
                                                                         StringBuilder sb = new StringBuilder();
                                                                         sb.append("结果：").append("\n")
                                                                                 .append("resultCode：").append(bundle.getInt("extra_result_code", -1)).append("\n")
                                                                                 .append("resultMsg：").append(bundle.getString("extra_result_msg", "")).append("\n")
                                                                                 .append("token：").append(bundle.getString("extra_token", "")).append("\n")
                                                                                 .append("user_id").append(bundle.getString("extra_user_id"));

                                                                         mResult.setText(sb);
                                                             }
                                                         }

                                                         @Override
                                                         public void onFail(int i, Bundle bundle) {
                                                             StringBuilder sb = new StringBuilder();
                                                             sb.append("结果：").append("\n")
                                                                     .append("resultCode：").append(bundle.getInt("extra_result_code", -1)).append("\n")
                                                                     .append("resultMsg：").append(bundle.getString("extra_result_msg", "")).append("\n")
                                                                     .append("token：").append(bundle.getString("extra_token", "")).append("\n")
                                                                     .append("user_id").append(bundle.getString("extra_user_id"));
                                                             mResult.setText(sb);
                                                         }
                                                     }
                                             );
                                         }
                                     }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("renlei", "onActivityResult" + requestCode + "requestCode" + resultCode + "data" + data);
        if (data != null) {
            Log.d("renlei", "token" + data.getStringExtra("extra_token"));
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("结果：").append("\n")
                        .append("resultCode：").append(bundle.getInt("extra_result_code", -1)).append("\n")
                        .append("resultMsg：").append(bundle.getString("extra_result_msg", "")).append("\n")
                        .append("token：").append(bundle.getString("extra_token", "")).append("\n")
                        .append("user_id").append(bundle.getString("extra_user_id"));
                mResult.setText(sb);
            }
        }
    }

    @Override
    protected void onDestroy() {
//        AuthManager.getInstance().release();
        super.onDestroy();
    }
}
