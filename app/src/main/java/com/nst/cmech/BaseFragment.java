package com.nst.cmech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nst.cmech.util.ToastHelper;
import com.nst.cmech.view.LoadingDialog;


public abstract class BaseFragment extends BaseLazyFragment {

    private LoadingDialog loadingDialog;

    /**
     * 转跳 没有finish 没有切换效果
     *
     * @param classObj
     */
    public void toActivity(Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(mActivity, classObj);
        startActivity(intent);
    }

    /**
     * 转跳 没有finish
     *
     * @param classObj
     */
    public void overlay(Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(mActivity, classObj);
        startActivity(intent);
    }

    /**
     * 转跳 没有finish
     *
     * @param classObj
     * @param params
     */
    public void overlay(Class<?> classObj, Bundle params) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(mActivity, classObj);
        intent.putExtras(params);
        startActivity(intent);
//        mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    /**
     * 转跳 没有finish
     *
     * @param classObj
     * @param requestCode
     */
    public void overlayForResult(Class<?> classObj, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(mActivity, classObj);
        startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }
    // //////////////////////////////////////////////////////////////////////////////////////////////
    // logic method

    /**
     * 转跳 没有finish
     *
     * @param classObj
     * @param requestCode
     * @param params
     */
    public void overlayForResult(Class<?> classObj, int requestCode, Bundle params) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(mActivity, classObj);
        intent.putExtras(params);
        startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    /**
     * 转跳 没有finish
     *
     * @param classObj
     * @param requestCode
     * @param params
     */
    public void overlayLeftForResult(Class<?> classObj, int requestCode, Bundle params) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(mActivity, classObj);
        intent.putExtras(params);
        startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    /**
     * 转跳 有finish
     *
     * @param classObj
     */
    public void forward(Class<?> classObj) {
        Intent intent = new Intent();
        intent.setClass(mActivity, classObj);
        this.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        mActivity.finish();
    }
    // //////////////////////////////////////////////////////////////////////////////////////////////
    // debug method

    /**
     * 转跳 有finish
     *
     * @param classObj
     * @param params
     */
    public void forward(Class<?> classObj, Bundle params) {
        Intent intent = new Intent();
        intent.setClass(mActivity, classObj);
        intent.putExtras(params);
        this.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        mActivity.finish();
    }

    public void startAndClearAll(Class<?> classObj) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(mActivity, classObj);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public final String TAG = this.getClass().getSimpleName();

    protected void showDialog(String title) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity(), title);
            loadingDialog.setCancelable(true);
            loadingDialog.show();
        } else {
            loadingDialog.show(title);
        }
    }

    protected void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void toast(String msg) {
        ToastHelper.showToast(msg, getActivity());
    }

    protected void toast(int msg_id) {
        ToastHelper.showToast(getString(msg_id), getActivity());
    }

    /**
     * 查找view
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
