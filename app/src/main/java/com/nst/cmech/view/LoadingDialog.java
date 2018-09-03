package com.nst.cmech.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.nst.cmech.R;


/**
 * Created by Emir on 2016/7/28.
 */
public class LoadingDialog extends BaseDialog {

    private TextView textTitle;
    private String text;

    public LoadingDialog(Context context, String text) {
        super(context);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_loading);
        textTitle= findViewById(R.id.text_title);
        if(!TextUtils.isEmpty(text)){
            textTitle.setText(text);
        }
    }

    /**
     * 对外显示方法
     * @param text 提示语
     */
    public void show(String text)
    {
        show();
        this.text=text;
        textTitle.setText(text);
    }

}
