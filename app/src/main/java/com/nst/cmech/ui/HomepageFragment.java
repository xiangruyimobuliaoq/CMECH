package com.nst.cmech.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nst.cmech.R;
import com.nst.cmech.bean.Module;
import com.nst.cmech.bean.NewsList;
import com.nst.cmech.bean.Resp;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.DpUtil;
import com.nst.cmech.util.GlideApp;
import com.nst.cmech.util.SpUtil;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;
import com.tmall.ultraviewpager.UltraViewPager;

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
@Layout(layoutId = R.layout.fragment_homepage)
public class HomepageFragment extends com.nst.cmech.BaseFragment {
    @BindView(R.id.banner)
    protected UltraViewPager banner;
    @BindView(R.id.modules)
    protected RecyclerView modules;
    @BindView(R.id.company)
    protected TextView company;
    private ModuleAdapter moduleAdapter;


    @Override
    protected void init() {
        moduleAdapter = new ModuleAdapter(R.layout.item_modules, null);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        modules.setLayoutManager(layoutManager);
        modules.setAdapter(moduleAdapter);
        company.setText(ConsUtil.getCompany());
        moduleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Module.DataClass dataClass = (Module.DataClass) adapter.getItem(position);
                Bundle params = new Bundle();
                params.putSerializable(ConsUtil.DATACLASS, dataClass);
                if (dataClass.flag == 1) {
                    overlay(FileDownloadActivity.class, params);
                } else {
                    overlay(ModuleListActivity.class, params);
                }
            }
        });
        moduleAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        requestData();
    }

    private void requestData() {
        OkGo.<String>post(Url.module).upJson(new Gson().toJson(new Params("0"))).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Resp<Module> resp = new Gson().fromJson(response.body(), new TypeToken<Resp<Module>>() {
                }.getType());
                if (resp.status == 1) {
                    moduleAdapter.replaceData(resp.data.dataClass);
                }
            }
        });
        OkGo.<String>post(Url.news).upJson(new Gson().toJson(new Limit(3))).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Resp<NewsList> resp = new Gson().fromJson(response.body(), new TypeToken<Resp<NewsList>>() {
                }.getType());
                if (resp.status == 1) {
                    initBanner(resp.data.news);
                }
            }
        });
    }

    private class Params {
        public String id;

        public Params(String id) {
            this.id = id;
        }
    }

    private class Limit {
        public int limit;

        public Limit(int id) {
            this.limit = id;
        }
    }

    private void initBanner(List<NewsList.News> news) {
        banner.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        PagerAdapter adapter = new BannerAdapter(news);
        banner.setAdapter(adapter);
        banner.initIndicator();
//设置indicator样式
        banner.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(getResources().getColor(R.color.icon1))
                .setNormalColor(getResources().getColor(R.color.icon2))
                .setIndicatorPadding(DpUtil.dip2px(mContext, 10))
                .setRadius(DpUtil.dip2px(mContext, 5));
//设置indicator对齐方式
        banner.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//构造indicator,绑定到UltraViewPager
        banner.getIndicator().build();
        banner.setItemMargin(0, 0, 0, DpUtil.dip2px(mContext, 20));
//设定页面循环播放
        banner.setInfiniteLoop(true);
        banner.setOffscreenPageLimit(news.size());
//设定页面自动切换  间隔2秒
        banner.setAutoScroll(6000);
    }


    class BannerAdapter extends PagerAdapter {
        List<NewsList.News> news;

        public BannerAdapter(List<NewsList.News> news) {
            this.news = news;
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner, null);
            container.addView(linearLayout);
            RoundedImageView iv = linearLayout.findViewById(R.id.iv);
            GlideApp.with(HomepageFragment.this)
                    .load(Url.file + news.get(position).picture)
                    .centerCrop()
                    .placeholder(R.mipmap.default_map1)
                    .error(R.mipmap.default_map1)
                    .into(iv);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", news.get(position).id);
                    overlay(NewsActivity.class, bundle);
                }
            });
            return linearLayout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    class ModuleAdapter extends BaseQuickAdapter<Module.DataClass, BaseViewHolder> {
        public ModuleAdapter(int layoutResId, @Nullable List<Module.DataClass> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Module.DataClass item) {
            helper.setText(R.id.name, SpUtil.getInt("language", "language", 0) == 2 ? item.ename : item.name);
            RoundedImageView iv = helper.getView(R.id.iv);
            GlideApp.with(HomepageFragment.this)
                    .load(Url.file + item.imageUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.default_map)
                    .error(R.mipmap.default_map)
                    .into(iv);
        }
    }
}
