package com.nst.cmech.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nst.cmech.App;
import com.nst.cmech.BaseActivity;
import com.nst.cmech.R;
import com.nst.cmech.bean.Login;
import com.nst.cmech.bean.Resp;
import com.nst.cmech.bean.req.LoginReq;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.SpUtil;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/21 下午1:38
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @BindView(R.id.username)
    protected EditText username;
    @BindView(R.id.password)
    protected EditText password;
    @BindView(R.id.login)
    protected Button login;
    public LocationClient mLocationClient;
    private BDAbstractLocationListener mBdAbstractLocationListener;
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void init() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        mBdAbstractLocationListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                mLatitude = bdLocation.getLatitude();
                mLongitude = bdLocation.getLongitude();
                Log.e("123", mLatitude + "     " + mLongitude);
            }
        };

        UIUtil.setStatusBar(this, getResources().getColor(R.color.background));
        requestPermissions();
    }

    @OnClick(R.id.login)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
        }
    }

    private void login() {
        final String username = this.username.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            toast(R.string.error_emptyusername);
            return;
        }
        showDialog(getString(R.string.text_progress));
        OkGo.<String>post(Url.login).upJson(new Gson().toJson(new LoginReq(username, password, mLatitude, mLongitude))).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dismissDialog();
                Resp<Login> resp = new Gson().fromJson(response.body(), new TypeToken<Resp<Login>>() {
                }.getType());
                toast(SpUtil.getInt("language", "language", 0) == 2 ? resp.emessage : resp.message);
                if (resp.status == 1) {
                    SpUtil.putString(ConsUtil.USERINFO, ConsUtil.CLIENTID, resp.data.token);
                    SpUtil.putString(ConsUtil.USERINFO, ConsUtil.USERNAME, username);
                    SpUtil.putString(ConsUtil.USERINFO, ConsUtil.COMPANY, resp.data.user.company);
                    ConsUtil.setLogin(true);
                    App.initOkgo();
                    forward(MainActivity.class);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.unRegisterLocationListener(mBdAbstractLocationListener);
        mLocationClient.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.registerLocationListener(mBdAbstractLocationListener);
        mLocationClient.start();
    }
}
