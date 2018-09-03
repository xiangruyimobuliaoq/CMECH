package com.nst.cmech.ui;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nst.cmech.BaseActivity;
import com.nst.cmech.R;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.view.Layout;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 下午5:09
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_search)
public class SearchActivity extends BaseActivity {
    @BindView(R.id.content)
    protected EditText content;
    @BindView(R.id.cancel)
    protected TextView cancel;
    @BindView(R.id.close)
    protected ImageView close;
    @BindView(R.id.list)
    protected RecyclerView list;

    @Override
    protected void init() {
        UIUtil.setStatusBar(this, getResources().getColor(R.color.background));
        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(content.getText().toString().trim());
                    dismissKeyboard();
                    return true;
                }
                return false;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard();
                finish();
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    close.setVisibility(View.GONE);
                } else {
                    close.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
            }
        });
    }

    private void search(String s) {

    }
}
