package com.xiaomi.smarthome.auth;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });
    }
    private void createFile() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/smarthome_record/" + new SimpleDateFormat("yyyy_mm_dd").format(new Date())+"_____"+new SimpleDateFormat("HH_mm_dd").format(new Date())+"log.txt";
        File file = new File(path);
        try {

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream ous = new FileOutputStream(file);
            if (ous != null){
                Log.d("MainActivity","成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
