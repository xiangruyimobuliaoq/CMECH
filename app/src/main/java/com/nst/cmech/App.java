package com.nst.cmech;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.nst.cmech.ui.MainActivity;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.DbUtil;
import com.nst.cmech.util.UIUtil;

import java.util.Locale;


/**
 * 创建者     彭龙
 * 创建时间   2018/6/14 下午3:35
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class App extends Application {

    private static boolean isAppInForeground;
    private static int started;
    private static int stopped;

    @Override
    public void onCreate() {
        super.onCreate();
        UIUtil.init(this);
        DbUtil.init(this);
        OkGo.getInstance().init(this);
        initOkgo();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (isAppShowFromBackground()) {
                    isAppInForeground = true;
                }
                ++started;
                if (isAppInForeground()) {
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ++stopped;
                if (isAppInBackground()) {
                    isAppInForeground = false;
                }
                if (isAppInBackground()) {
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            private boolean isAppShowFromBackground() {
                return started == stopped;
            }

            //外部调用
            public boolean isAppInForeground() {
                return isAppInForeground;
            }

            public boolean isAppInBackground() {
                return started == stopped;
            }
        });
    }

    public static void initOkgo() {
        if (!TextUtils.isEmpty(ConsUtil.getID())) {
            HttpHeaders headers = new HttpHeaders();
            headers.put("token", ConsUtil.getID());
            OkGo.getInstance().addCommonHeaders(headers);
        }
    }


}