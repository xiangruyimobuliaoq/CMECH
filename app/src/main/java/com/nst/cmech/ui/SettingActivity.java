package com.nst.cmech.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nst.cmech.BaseAppActivity;
import com.nst.cmech.R;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.view.Layout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 上午11:18
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_setting)
public class SettingActivity extends BaseAppActivity {
    @BindView(R.id.changepwd)
    protected TextView changepwd;
    @BindView(R.id.yuyanshezhi)
    protected TextView yuyanshezhi;
    @BindView(R.id.username)
    protected TextView username;
    @BindView(R.id.exit)
    protected TextView exit;

    @Override
    protected void init() {
        UIUtil.setStatusBar(this, getResources().getColor(R.color.background));
        setTitle(R.string.text_mypage);
        username.setText(ConsUtil.getUsername());
    }

    @OnClick({R.id.changepwd, R.id.yuyanshezhi, R.id.exit})
    public void onClick(View view) {
        if (view.getId() == R.id.changepwd) {
            overlay(ChangePwdActivity.class);
        } else if (view.getId() == R.id.yuyanshezhi) {
            overlay(LanguageSettingActivity.class);
        } else if (view.getId() == R.id.exit) {
            new MaterialDialog.Builder(this).title(R.string.text_tips)
                    .content(R.string.text_logout).positiveText(R.string.text_logout_ok)
                    .negativeText(R.string.text_logout_cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ConsUtil.setLogin(false);
                            startAndClearAll(LoginActivity.class);
                        }
                    }).show();
        }
    }
}
