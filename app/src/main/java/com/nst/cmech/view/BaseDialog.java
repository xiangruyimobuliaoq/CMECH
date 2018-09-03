package com.nst.cmech.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Emir on 2016/7/27.
 */
public class BaseDialog extends Dialog {
    protected Context context;
    public BaseDialog(Context context)
    {
        super(context, android.R.style.Theme_Translucent);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

}

