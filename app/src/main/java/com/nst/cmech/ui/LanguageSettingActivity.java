package com.nst.cmech.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.nst.cmech.BaseAppActivity;
import com.nst.cmech.R;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.SpUtil;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.view.Layout;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 上午11:51
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_languagesetting)
public class LanguageSettingActivity extends BaseAppActivity {
    @BindView(R.id.cancel)
    protected TextView cancel;
    @BindView(R.id.done)
    protected TextView done;
    @BindView(R.id.chn)
    protected TextView chn;
    @BindView(R.id.eng)
    protected TextView eng;
    private int current;

    @Override
    protected void init() {
        UIUtil.setStatusBar(this, getResources().getColor(R.color.background));
        setTitle(R.string.text_yuyanshezhi);
        cancel.setText(R.string.text_cancel);
        done.setText(R.string.text_done);
        int i = SpUtil.getInt("language", "language", 0);
        if (i == 0 || i == 1) {
            chn.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.selected), null);
            eng.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            current = 1;
        } else {
            eng.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.selected), null);
            chn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            current = 2;
        }
    }

    @OnClick({R.id.cancel, R.id.done, R.id.chn, R.id.eng})
    public void onClick(View view) {
        if (view.getId() == R.id.cancel) {
            finish();
        } else if (view.getId() == R.id.done) {
            switch (current) {
                case 1:
                    ConsUtil.setLanguage(1);
                    changeAppLanguage(Locale.SIMPLIFIED_CHINESE);
                    break;
                case 2:
                    ConsUtil.setLanguage(2);
                    changeAppLanguage(Locale.ENGLISH);
                    break;
            }
        } else if (view.getId() == R.id.chn) {
            chn.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.selected), null);
            eng.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            current = 1;
        } else if (view.getId() == R.id.eng) {
            eng.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.selected), null);
            chn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            current = 2;
        }
    }

    @Override
    protected boolean getHomeButtonEnable() {
        return false;
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
        //重新启动Activity
        startAndClearAll(MainActivity.class);
    }
}
