package com.nst.cmech.ui;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nst.cmech.BaseAppActivity;
import com.nst.cmech.R;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/2 下午5:00
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_webview)
public class NewsActivity extends BaseAppActivity {
    @BindView(R.id.webview)
    protected WebView mWebView;

    @Override
    protected void init() {
        int id = getIntent().getIntExtra("id", 0);
        UIUtil.setStatusBar(this, getResources().getColor(R.color.background));
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(Url.newsDetail + "?id=" + id);
    }
}
