package com.nst.cmech;


import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

import com.nst.cmech.ui.LoginActivity;
import com.nst.cmech.ui.MainActivity;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.view.Layout;

import java.util.Locale;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/10 上午9:17
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_root)
public class RootActivity extends LaunchActivity {
    @Override
    protected void init() {
        changeAppLanguage(ConsUtil.getLanguage());

    }


    public void changeAppLanguage(Locale locale) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        getResources().updateConfiguration(configuration, metrics);
        if (ConsUtil.getLogin()) {
            forward(MainActivity.class);
        } else
            forward(LoginActivity.class);
    }
}
