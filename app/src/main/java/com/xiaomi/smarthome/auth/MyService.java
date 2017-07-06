package com.xiaomi.smarthome.auth;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by renlei
 * DATE: 16-3-7
 * Time: 下午4:15
 * Email: lei.ren@renren-inc.com
 */
public class MyService extends Service {
    private static final int RECEIVE_MESSAGE_CODE = 0x0001;

    private static final int SEND_MESSAGE_CODE = 0x0002;
    private Messenger serviceMessenger = new Messenger(new ServiceHandle());
    private Messenger clientMessenger;

    class ServiceHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("MyService", "ServiceHandler -> handleMessage");
            Log.d("MyService", "android.os.Process.myPid();" + android.os.Process.myPid());

            if (msg.what == RECEIVE_MESSAGE_CODE){
                Bundle bundle = msg.getData();
                if (bundle!=null){
                    Log.d("MyService","收到客户端的消息:"+bundle.getString("msg"));
                }
                clientMessenger = msg.replyTo;
                if (clientMessenger!=null){
                    Log.d("MyService","Service 向客户端回信");
                    Message message = Message.obtain();
                    message.what = SEND_MESSAGE_CODE;
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("msg","hello client 我是Service");
                    message.setData(bundle1);
                    try {
                        clientMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.e("MyService", "MyService -> onDestroy");
        clientMessenger = null;
        super.onDestroy();
    }
    @Override
    public void onCreate() {
        Log.e("MyService", "MyService -> onCreate");
        Log.e("MyService", "android.os.Process.myPid();" + android.os.Process.myPid());

        super.onCreate();
    }
}
