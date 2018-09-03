package com.nst.cmech.util;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nst.cmech.BaseActivity;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/13 下午3:15
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class DialogUtil {
    private static MaterialDialog mProgressDialog;

    public static void showProgress(BaseActivity context) {
        mProgressDialog = new MaterialDialog.Builder(context)
                .title("正在处理中")
                .content("请稍后...")
                .progress(false, 0)
                .show();
    }

    public static void dismissProgress() {
        if (null == mProgressDialog || mProgressDialog.isCancelled())
            return;
        mProgressDialog.dismiss();
    }
}
