package com.nst.cmech.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nst.cmech.R;
import com.nst.cmech.bean.NewsList;
import com.nst.cmech.bean.Resp;
import com.nst.cmech.util.DateUtil;
import com.nst.cmech.util.DpUtil;
import com.nst.cmech.util.GlideApp;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 上午9:35
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.fragment_news)
public class NewsFragment extends com.nst.cmech.BaseFragment {
    @BindView(R.id.refresh)
    protected SwipeRefreshLayout refresh;
    @BindView(R.id.list)
    protected RecyclerView list;
    private View mEmptyView;
    private View mErrorView;
    private NewsAdapter newsAdapter;


    @Override
    protected void init() {
        mEmptyView = getLayoutInflater().inflate(R.layout.layout_emptyview, null);
        mErrorView = getLayoutInflater().inflate(R.layout.layout_nonetworkview, null);
        newsAdapter = new NewsAdapter(R.layout.item_news, null);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(newsAdapter);
        requestDatas();
        refresh.setColorSchemeColors(getResources().getColor(R.color.icon1),
                getResources().getColor(R.color.icon2)
        );
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDatas();
            }
        });
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDatas();
            }
        });

        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", ((NewsList.News) adapter.getItem(position)).id);
                overlay(NewsActivity.class, bundle);
            }
        });
    }

    private void requestDatas() {
        OkGo.<String>post(Url.news).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                refresh.setRefreshing(false);
                Resp<NewsList> resp = new Gson().fromJson(response.body(), new TypeToken<Resp<NewsList>>() {
                }.getType());
                if (resp.status == 1 && null != resp.data) {
                    newsAdapter.replaceData(resp.data.news);
                    if (resp.data.news.size() == 0) {
                        newsAdapter.setEmptyView(mEmptyView);
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                refresh.setRefreshing(false);
                newsAdapter.setEmptyView(mErrorView);
            }
        });

    }


    class NewsAdapter extends BaseQuickAdapter<NewsList.News, BaseViewHolder> {

        public NewsAdapter(int layoutResId, @Nullable List<NewsList.News> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, NewsList.News item) {
            helper.setText(R.id.title, item.title)
                    .setText(R.id.content, item.details)
                    .setText(R.id.time, new SimpleDateFormat(DateUtil.YMD_HMS).format(new java.util.Date(item.createtime)));
            RoundedImageView iv = helper.getView(R.id.iv);
            GlideApp.with(NewsFragment.this)
                    .load(Url.file + item.picture)
                    .override(DpUtil.dip2px(UIUtil.mContext, 100), DpUtil.dip2px(UIUtil.mContext, 70))
                    .centerCrop()
                    .placeholder(R.mipmap.default_map1)
                    .error(R.mipmap.default_map1)
                    .into(iv);
        }
    }
}
