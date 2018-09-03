package com.nst.cmech;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.nst.cmech.util.ToastHelper;
import com.nst.cmech.view.Layout;
import com.nst.cmech.view.LoadingDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;


import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * 创建者     彭龙
 * 创建时间   2018/6/14 下午3:36
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        Layout layout = getClass().getAnnotation(Layout.class);
        if (layout.layoutId() > 0) {
            setContentView(layout.layoutId());
        }
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        init();
    }

    protected abstract void init();

    public void toast(String msg) {
        ToastHelper.showToast(msg, this);
    }

    protected void toast(int msg_id) {
        ToastHelper.showToast(getString(msg_id), this);
    }

    protected void initToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * @param content
     */
    protected void showSnackBarForever(String content) {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        Snackbar.make(view.getChildAt(0), content, Snackbar.LENGTH_INDEFINITE)
                .setAction("关闭", null).show();
    }

    protected void showSnackBar(String content) {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        Snackbar.make(view.getChildAt(0), content, Snackbar.LENGTH_LONG)
                .setAction("关闭", null).show();
    }

    /**
     * 转跳 没有finish 没有切换效果
     *
     * @param classObj
     */
    public void toActivity(Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(this, classObj);
        startActivity(intent);
    }

    /**
     * 转跳 没有finish
     *
     * @param classObj
     */
    public void overlay(Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(this, classObj);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 转跳 没有finish
     *
     * @param classObj
     * @param params
     */
    public void overlay(Class<?> classObj, Bundle params) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(this, classObj);
        intent.putExtras(params);
        startActivity(intent);
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
        intent.setClass(this, classObj);
        intent.putExtras(params);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 转跳 没有finish
     *
     * @param classObj
     * @param requestCode
     */
    public void overlayForResult(Class<?> classObj, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(this, classObj);
        startActivityForResult(intent, requestCode);
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
        intent.setClass(this, classObj);
        intent.putExtras(params);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 转跳 有finish
     *
     * @param classObj
     */
    public void forward(Class<?> classObj) {
        Intent intent = new Intent();
        intent.setClass(this, classObj);
        startActivity(intent);
        finish();
    }

    public void startAndClearAll(Class<?> classObj) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, classObj);
        startActivity(intent);
    }

    protected void showDialog(String title) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, title);
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

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    public void dismissKeyboard() {
        try {
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText())
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void requestPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            finish();
                        } else {
                            getAppDetailSettingIntent();
                            finish();
                        }
                    }
                });
    }

    public void getAppDetailSettingIntent(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }
}
