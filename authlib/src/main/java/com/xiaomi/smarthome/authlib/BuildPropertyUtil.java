package com.xiaomi.smarthome.authlib;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Time 2017/7/5.
 * User renlei
 * Email renlei@xiaomi.com
 */

public class BuildPropertyUtil {
    private Properties properties = null;
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";

    public BuildPropertyUtil() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEMUI() {
        if (isPropertiesExist(KEY_EMUI_VERSION_CODE) || Build.MANUFACTURER.equalsIgnoreCase("HUAWEI")) {
            return true;
        }
        return false;
    }

    public boolean isFlyme() {
        try {
            if (Build.MANUFACTURER.equalsIgnoreCase("Meizu")) {
                return true;
            }

            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    private boolean isPropertiesExist(String... keys) {
        if (properties == null)
            return false;

        for (String key : keys) {
            String str = properties.getProperty(key);
            if (str == null)
                return false;
        }
        return true;
    }

}
