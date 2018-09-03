package com.nst.cmech.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nst.cmech.BaseAppActivity;
import com.nst.cmech.R;
import com.nst.cmech.bean.FileModule;
import com.nst.cmech.bean.Module;
import com.nst.cmech.bean.Resp;
import com.nst.cmech.util.ConsUtil;
import com.nst.cmech.util.DbUtil;
import com.nst.cmech.util.DpUtil;
import com.nst.cmech.util.GlideApp;
import com.nst.cmech.util.SpUtil;
import com.nst.cmech.util.UIUtil;
import com.nst.cmech.util.Url;
import com.nst.cmech.view.Layout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

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
public class FileDownloadActivity extends BaseAppActivity {
    @BindView(R.id.search)
    protected ImageView search;
    @BindView(R.id.style)
    protected ImageView style;
    @BindView(R.id.list)
    protected RecyclerView lists;
    private ModuleAdapter moduleAdapter;
    //    private GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    private Module.DataClass mDataClass;
    private View mEmptyView;
    private View mErrorView;

    private OkDownload mOkDownload;
    private List<Module.DataClass> mDataInfo;


    @Override
    protected void init() {
        mDataClass = (Module.DataClass) getIntent().getSerializableExtra(ConsUtil.DATACLASS);
        setTitle(SpUtil.getInt("language", "language", 0) == 2 ? mDataClass.ename : mDataClass.name);
        style.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        mEmptyView = getLayoutInflater().inflate(R.layout.layout_emptyview, null);
        mErrorView = getLayoutInflater().inflate(R.layout.layout_nonetworkview, null);
        moduleAdapter = new ModuleAdapter(null);
        moduleAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        if (ConsUtil.getStyle() == 1) {
//            style.setImageResource(R.mipmap.nav_list_1);
//            lists.setLayoutManager(mGridLayoutManager);
//        } else {
//            style.setImageResource(R.mipmap.nav_list);
        lists.setLayoutManager(mLinearLayoutManager);
//        }
        lists.setAdapter(moduleAdapter);
        requestData();
        mOkDownload = OkDownload.getInstance();
        mOkDownload.setFolder(Environment.getExternalStorageDirectory().getPath() + "/CMECH/");
        mOkDownload.getThreadPool().setCorePoolSize(3);
    }

    private void requestData() {
        showDialog(getString(R.string.text_progress));
        OkGo.<String>post(Url.fileList).upJson(new Gson().toJson(new Params(mDataClass.id))).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Resp<FileModule> resp = new Gson().fromJson(response.body(), new TypeToken<Resp<FileModule>>() {
                }.getType());
//                toast(resp.message);
                dismissDialog();
                if (resp.status == 1 && null != resp.data) {
                    mDataInfo = resp.data.dataInfo;
                    moduleAdapter.replaceData(mDataInfo);
                    if (resp.data.dataInfo.size() == 0) {
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

    private class Params {
        public int dataType;

        public Params(int dataType) {
            this.dataType = dataType;
        }
    }

//    @OnClick({R.id.search, R.id.style})
//    public void onClick(View view) {
//        if (view.getId() == R.id.search) {
//            overlay(SearchActivity.class);
//        } else if (view.getId() == R.id.style) {
//            if (ConsUtil.getStyle() == 1) {
//                ConsUtil.setStyle(2);
//                style.setImageResource(R.mipmap.nav_list);
//                lists.setLayoutManager(mLinearLayoutManager);
//            } else {
//                ConsUtil.setStyle(1);
//                style.setImageResource(R.mipmap.nav_list_1);
//                lists.setLayoutManager(mGridLayoutManager);
//            }
//            lists.setAdapter(moduleAdapter);
//        }
//    }

    class ModuleAdapter extends BaseMultiItemQuickAdapter<Module.DataClass, BaseViewHolder> {
        public ModuleAdapter(@Nullable List<Module.DataClass> data) {
            super(data);
            addItemType(0, R.layout.item_filedownload);
        }

        @Override
        protected void convert(BaseViewHolder helper, final Module.DataClass item) {
            final TextView btn = helper.getView(R.id.downOrOpen);
            final TextView progress = helper.getView(R.id.progress);
            if (TextUtils.isEmpty(item.file)) {
                btn.setText(R.string.text_nofile);
            } else {
                ArrayList<Module.DataClass> list = DbUtil.single().query(new QueryBuilder<>(Module.DataClass.class)
                        .whereEquals("id", item.id));
                Log.e("123", "11111");
                if (list.size() > 0) {
                    //数据库有记录
                    Log.e("123", "22222222");
                    final Module.DataClass dataClass = list.get(0);
                    Log.e("123", new Gson().toJson(dataClass));
                    if (dataClass.filepath == null || dataClass.version < item.version) {
                        //数据库没有文件路径或者本地版本号低于服务器
                        btn.setText(R.string.text_download);
                        helper.setOnClickListener(R.id.downOrOpen, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //下载
                                download(item.id, item.file, btn, progress, item);
                            }
                        });
                    } else {
                        File file = new File(dataClass.filepath);
                        Log.e("123", file.exists() + "");
                        if (file.exists()) {
                            btn.setText(R.string.text_open);
                            progress.setText(dataClass.filepath);
                            helper.setOnClickListener(R.id.downOrOpen, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //下载
                                    show(dataClass.filepath);
                                }
                            });
                        } else {
                            btn.setText(R.string.text_download);
                            helper.setOnClickListener(R.id.downOrOpen, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //下载
                                    download(item.id, item.file, btn, progress, item);
                                }
                            });
                        }
                    }
                } else {
                    //数据库没有记录
                    btn.setText(R.string.text_download);
                    helper.setOnClickListener(R.id.downOrOpen, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //下载
                            download(item.id, item.file, btn, progress, item);
                        }
                    });
                }
            }
            helper.setText(R.id.name, item.dataName);
            ImageView iv = helper.getView(R.id.iv);
            GlideApp.with(FileDownloadActivity.this)
                    .load(Url.file + item.fileImage)
                    .placeholder(R.mipmap.default_map1)
                    .error(R.mipmap.default_map1)
                    .into(iv);
        }
    }

    private void download(final int id, String file, final TextView btn, final TextView tv, final Module.DataClass item) {
        GetRequest<File> request = new GetRequest<>(Url.down + file);
        DownloadTask task = mOkDownload.request(String.valueOf(id), request).save().register(new DownloadListener(id) {
            @Override
            public void onStart(Progress progress) {
                btn.setText(R.string.text_ondown);
                btn.setOnClickListener(null);
            }

            @Override
            public void onProgress(Progress progress) {
                tv.setText((progress.currentSize / 1024 / 1024) +
                        "MB/" + (progress.totalSize / 1024 / 1024) + "MB");
            }

            @Override
            public void onError(Progress progress) {
                btn.setText(R.string.text_retry);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新下载
                        mOkDownload.getTask(String.valueOf(id)).restart();
                    }
                });
            }

            @Override
            public void onFinish(final File file, Progress progress) {
                tv.setText(file.getAbsolutePath());
                btn.setText(R.string.text_open);
                item.filepath = file.getAbsolutePath();
                Log.e("123", item.filepath);
                DbUtil.single().save(item);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //打开
                        show(file.getAbsolutePath());
                    }
                });
            }

            @Override
            public void onRemove(Progress progress) {

            }
        });
        task.start();
    }

    private void show(String file) {
        try {
            Uri uri = null;
//            if (Build.VERSION.SDK_INT >= 24) {
//                uri = FileProvider.getUriForFile(this, "com.nst.cmech.provider", new File(file));
//            } else {
            uri = Uri.fromFile(new File(file));
//            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, ConsUtil.getMIMEType(file));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        for (Module.DataClass data :
                mDataInfo) {
            DownloadTask task = mOkDownload.getTask(String.valueOf(data.id));
            if (null != task) {
                task.unRegister(String.valueOf(data.id));
            }
        }
        super.onDestroy();
    }
}
