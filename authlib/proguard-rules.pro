# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\soft\Andriod\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5               # 指定代码的压缩级别
-dontusemixedcaseclassnames         # 是否使用大小写混合
-dontskipnonpubliclibraryclasses    # 是否混淆第三方jar
-dontpreverify                      # 混淆时是否做预校验
-verbose                            # 混淆时是否记录日志
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep class com.xiaomi.smarthome.authlib.IAuthMangerImpl{
        *;
       #public abstract boolean init(android.content.Context);
       #public abstract boolean callAuth(android.content.Context, android.os.Bundle,int,com.xiaomi.smarthome.authlib.IAuthResponse);
       #public static com.xiaomi.smarthome.authlib.IAuthMangerImpl getInstance();
       #public abstract void release();
       #public abstract void intiWithCallBack(android.content.Context,com.xiaomi.smarthome.authlib.IInitCallBack);
}
-keep class  com.xiaomi.smarthome.authlib.AuthCode{*;}
-keep class  com.xiaomi.smarthome.authlib.AuthConstants{*;}
-keep interface  com.xiaomi.smarthome.authlib.IAuthResponse{*;}
-keep class com.xiaomi.smarthome.authlib.IInitCallBack{*;}
-keep class com.xiaomi.smarthome.auth.model.AuthCallBackInfo{*;}
