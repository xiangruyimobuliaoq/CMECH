package com.nst.cmech.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.nst.cmech.BaseAppActivity;
import com.nst.cmech.BaseFragment;
import com.nst.cmech.R;
import com.nst.cmech.view.Layout;
import com.nst.cmech.view.NoScrollViewPager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/21 下午6:03
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_main)
public class MainActivity extends BaseAppActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.rg_main)
    protected RadioGroup mPageSelector;
    @BindView(R.id.pager)
    protected NoScrollViewPager mFragmentContainer;
    @BindView(R.id.img)
    protected ImageView img;

    private HomeFragmentAdapter mAdapter;
    private Map<Integer, BaseFragment> mCache = new HashMap<>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean getHomeButtonEnable() {
        return false;
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.text_homePage));
        initEvent();
        requestPermissions();


    }



    @OnClick(R.id.img)
    public void onClick(View view) {
        if (view.getId() == R.id.img) {
            overlay(SettingActivity.class);
        }
    }


    private void initEvent() {
        mPageSelector.setOnCheckedChangeListener(this);
        mAdapter = new HomeFragmentAdapter(getSupportFragmentManager());
        mFragmentContainer.setAdapter(mAdapter);
        mFragmentContainer.setOnPageChangeListener(this);
        mFragmentContainer.setCurrentItem(0);
    }

    public Fragment getFragment(int position) {
        //加上缓存功能,优先取缓存
        BaseFragment fragment = mCache.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case 0:
                fragment = new HomepageFragment();
                break;
            case 1:
                fragment = new NewsFragment();
                break;
        }
        mCache.put(position, fragment);
        return fragment;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mPageSelector.check(R.id.home);
        } else if (position == 1) {
            mPageSelector.check(R.id.news);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.home) {
            mFragmentContainer.setCurrentItem(0);
            setActTitle(getString(R.string.text_homePage));
        } else if (checkedId == R.id.news) {
            mFragmentContainer.setCurrentItem(1);
            setActTitle(getString(R.string.text_news));
        }
    }

    class HomeFragmentAdapter extends FragmentStatePagerAdapter {

        public HomeFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
