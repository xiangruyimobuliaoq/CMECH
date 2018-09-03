package com.nst.cmech;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * Created by Emir on 2016/11/11.
 */

public abstract class BaseAppActivity extends BaseActivity {

    public Toolbar toolbar;
    public TextView actTitle;
    public final static int HANlDER_ACTIVITY_TIMEOUT = 10001;
    private int activity_timeout_millis=60000;

    protected void  setActivityTimeOutMillis(int millis)
    {
        this.activity_timeout_millis=millis;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        initToolbar();

    }
    protected void sendAcivityTimeOutMessage(int millis)
    {
        setActivityTimeOutMillis(millis);
        sendAcivityTimeOutMessage();
    }

    protected void sendAcivityTimeOutMessage(){
        mHandler.sendEmptyMessageDelayed(HANlDER_ACTIVITY_TIMEOUT,activity_timeout_millis);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initToolbar() {
        super.initToolbar(toolbar);
        setActTitle(getTitle());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(getHomeButtonEnable());
        actionBar.setDisplayHomeAsUpEnabled(getHomeButtonEnable());

    }

    public void setActTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) return;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            if (actTitle != null)
                actTitle.setText(title);
        }
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case HANlDER_ACTIVITY_TIMEOUT:
                    finish();
                    break;
                default:
                    dispatchOtherMessage(msg);
                    break;
            }


        }
    };
    protected  void dispatchOtherMessage(Message msg){

    }
    protected  boolean getHomeButtonEnable(){
        return true;
    }

}
