package com.nst.cmech.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nst.cmech.BaseActivity;
import com.nst.cmech.R;
import com.nst.cmech.bean.Module;
import com.nst.cmech.bean.NewsList;
import com.nst.cmech.bean.Search;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.DateUtil;
import com.nst.cmech.util.DpUtil;
import com.nst.cmech.util.GlideApp;
import com.nst.cmech.util.SpUtil;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 下午5:09
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_search)
public class SearchActivity extends BaseActivity {
    @BindView(R.id.content)
    protected EditText content;
    @BindView(R.id.cancel)
    protected TextView cancel;
    @BindView(R.id.close)
    protected ImageView close;
    @BindView(R.id.list)
    protected RecyclerView list;
    private View mEmptyView;
    private View mErrorView;
    private SearchAdapter searchadapter;

    @Override
    protected void init() {
        UIUtil.setStatusBar(this, getResources().getColor(R.color.background));
        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(content.getText().toString().trim());
                    dismissKeyboard();
                    return true;
                }
                return false;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard();
                finish();
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    close.setVisibility(View.GONE);
                } else {
                    close.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
            }
        });
        mEmptyView = getLayoutInflater().inflate(R.layout.layout_emptyview, null);
        mErrorView = getLayoutInflater().inflate(R.layout.layout_nonetworkview, null);
        searchadapter = new SearchAdapter(R.layout.item_search, null);
        searchadapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(searchadapter);
        searchadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Module.DataClass dataClass = (Module.DataClass) adapter.getItem(position);
                Bundle params = new Bundle();
                if (dataClass.dataType == 0) {
                    //文件
                    params.putSerializable(ConsUtil.DATAINFO, dataClass);
                    overlay(FileDownloadActivity.class, params);
                } else if (dataClass.dataType == 1) {
                    //文件夹
                    params.putSerializable(ConsUtil.DATACLASS, dataClass);
                    overlay(ModuleListActivity.class, params);

                }
            }
        });
    }

    private void search(String s) {
        showDialog(getString(R.string.text_progress));
        OkGo.<String>get(Url.search + s).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dismissDialog();
                Search search = new Gson().fromJson(response.body(), Search.class);
                List<Module.DataClass> list = new ArrayList<>();
                if (search.status == 1) {
                    for (Module.DataClass aClass : search.dataInfo
                            ) {
                        aClass.dataType = 0;
                        list.add(aClass);
                    }
                    for (Module.DataClass aClass : search.dataClass
                            ) {
                        aClass.dataType = 1;
                        list.add(aClass);
                    }
                    searchadapter.replaceData(list);
                    if (list.size() == 0) {
                        searchadapter.setEmptyView(mEmptyView);
                    }
                } else {
                    searchadapter.replaceData(list);
                    searchadapter.setEmptyView(mEmptyView);
                }
            }

            @Override
            public void onError(Response<String> response) {
                dismissDialog();
                searchadapter.setEmptyView(mErrorView);
            }
        });
    }

    class SearchAdapter extends BaseQuickAdapter<Module.DataClass, BaseViewHolder> {

        public SearchAdapter(int layoutResId, @Nullable List<Module.DataClass> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Module.DataClass item) {
            if (item.dataType == 0) {
                helper.setText(R.id.content,
                        SpUtil.getInt("language", "language", 0) == 2 ?
                                item.pathEname + "/" + item.dataName : item.pathName + "/" + item.dataName
                );
            } else if (item.dataType == 1) {
                helper.setText(R.id.content,
                        SpUtil.getInt("language", "language", 0) == 2 ?
                                item.pathEname + "/" + item.ename : item.pathName + "/" + item.name
                );
            }
        }
    }
}
