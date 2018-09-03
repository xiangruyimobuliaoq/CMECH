package com.nst.cmech.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nst.cmech.BaseAppActivity;
import com.nst.cmech.R;
import com.nst.cmech.bean.Resp;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 上午11:38
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_changepwd)
public class ChangePwdActivity extends BaseAppActivity {
    @BindView(R.id.oldpwd)
    protected EditText oldpwd;
    @BindView(R.id.newpwd)
    protected EditText newpwd;
    @BindView(R.id.newpwd2)
    protected EditText newpwd2;
    @BindView(R.id.done)
    protected Button done;

    @Override
    protected void init() {
        UIUtil.setStatusBar(this, getResources().getColor(R.color.background));
        setTitle(R.string.text_changepwd);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        String old = oldpwd.getText().toString().trim();
        String newp = newpwd.getText().toString().trim();
        String newp2 = newpwd2.getText().toString().trim();
        if (TextUtils.isEmpty(old) || TextUtils.isEmpty(newp) || TextUtils.isEmpty(newp2)) {
            toast(R.string.error_emptyusername);
            return;
        }
        if (!newp.equals(newp2)) {
            toast(getString(R.string.error_pwdnotsame));
            return;
        }
        showDialog(getString(R.string.text_progress));
        OkGo.<String>post(Url.changepwd).upJson(new Gson().toJson(new Params(ConsUtil.getUsername(), newp, old))).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dismissDialog();
                Resp resp = new Gson().fromJson(response.body(), Resp.class);
                toast(resp.message);
                if (resp.status == 1) {
                    finish();
                }
            }

            @Override
            public void onError(Response<String> response) {
                dismissDialog();
            }
        });
    }

    private class Params {
        public String username;
        public String password;
        public String oldpassword;

        public Params(String username, String password, String oldpassword) {
            this.username = username;
            this.password = password;
            this.oldpassword = oldpassword;
        }
    }
}
