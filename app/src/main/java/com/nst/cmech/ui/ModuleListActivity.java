package com.nst.cmech.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nst.cmech.BaseAppActivity;
import com.nst.cmech.R;
import com.nst.cmech.bean.Module;
import com.nst.cmech.bean.Resp;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.GlideApp;
import com.nst.cmech.util.SpUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 下午2:57
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_modulelist)
public class ModuleListActivity extends BaseAppActivity {
    @BindView(R.id.search)
    protected ImageView search;
    @BindView(R.id.style)
    protected ImageView style;
    @BindView(R.id.list)
    protected RecyclerView lists;
    private ModuleAdapter moduleAdapter;
    private GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    private Module.DataClass mDataClass;
    private View mEmptyView;
    private View mErrorView;


    @Override
    protected void init() {
        mDataClass = (Module.DataClass) getIntent().getSerializableExtra(ConsUtil.DATACLASS);
        setTitle(SpUtil.getInt("language", "language", 0) == 2 ? mDataClass.ename : mDataClass.name);
        mEmptyView = getLayoutInflater().inflate(R.layout.layout_emptyview, null);
        mErrorView = getLayoutInflater().inflate(R.layout.layout_nonetworkview, null);
        moduleAdapter = new ModuleAdapter(null);
        moduleAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        if (ConsUtil.getStyle() == 1) {
            style.setImageResource(R.mipmap.nav_list_1);
            lists.setLayoutManager(mGridLayoutManager);
        } else {
            style.setImageResource(R.mipmap.nav_list);
            lists.setLayoutManager(mLinearLayoutManager);
        }
        lists.setAdapter(moduleAdapter);
        requestData();

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
    }

    private void requestData() {
        showDialog(getString(R.string.text_progress));
        OkGo.<String>post(Url.module).upJson(new Gson().toJson(new Params(mDataClass.id))).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Resp<Module> resp = new Gson().fromJson(response.body(), new TypeToken<Resp<Module>>() {
                }.getType());
//                toast(resp.message);
                dismissDialog();
                if (resp.status == 1 && null != resp.data) {
                    moduleAdapter.replaceData(resp.data.dataClass);
                    setDataType(resp.data.dataClass);
                    if (resp.data.dataClass.size() == 0) {
                        moduleAdapter.setEmptyView(mEmptyView);
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                moduleAdapter.setEmptyView(mErrorView);
            }
        });

    }


    void setDataType(List<Module.DataClass> list) {
        if (null == list)
            return;
        for (Module.DataClass data :
                list) {
            if (ConsUtil.getStyle() == 1) {
                data.itemType = 1;
            } else {
                data.itemType = 2;
            }
        }
    }

    private class Params {
        public int id;

        public Params(int id) {
            this.id = id;
        }
    }

    @OnClick({R.id.search, R.id.style})
    public void onClick(View view) {
        if (view.getId() == R.id.search) {
            overlay(SearchActivity.class);
        } else if (view.getId() == R.id.style) {
            if (ConsUtil.getStyle() == 1) {
                ConsUtil.setStyle(2);
                style.setImageResource(R.mipmap.nav_list);
                lists.setLayoutManager(mLinearLayoutManager);
            } else {
                ConsUtil.setStyle(1);
                style.setImageResource(R.mipmap.nav_list_1);
                lists.setLayoutManager(mGridLayoutManager);
            }
            setDataType(moduleAdapter.getData());
            lists.setAdapter(moduleAdapter);
        }
    }

    class ModuleAdapter extends BaseMultiItemQuickAdapter<Module.DataClass, BaseViewHolder> {
        public ModuleAdapter(@Nullable List<Module.DataClass> data) {
            super(data);
            addItemType(1, R.layout.item_list_1);
            addItemType(2, R.layout.item_list_2);
        }

        @Override
        protected void convert(BaseViewHolder helper, Module.DataClass item) {
            helper.setText(R.id.name, SpUtil.getInt("language", "language", 0) == 2 ? item.ename : item.name);
            RoundedImageView iv = helper.getView(R.id.iv);
            GlideApp.with(ModuleListActivity.this)
                    .load(Url.file + item.imageUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.default_map1)
                    .error(R.mipmap.default_map1)
                    .into(iv);
        }
    }

}
