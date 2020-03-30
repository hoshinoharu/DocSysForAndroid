package com.rehoshi.docsys.ui;

import android.content.Intent;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseActivity;
import com.rehoshi.docsys.base.BaseFragment;
import com.rehoshi.docsys.ui.doc.DocFragment;
import com.rehoshi.docsys.ui.home.HomeFragment;
import com.rehoshi.docsys.ui.mine.MineHomeFragment;
import com.rehoshi.docsys.ui.subcribe.WebFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.btNv_bottomNav)
    BottomNavigationBar btNvBottomNav;
    @BindView(R.id.vwPg_houseHome)
    ViewPager vwPgHouseHome;

    private BaseFragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        fragments = new BaseFragment[]{
                new HomeFragment(),
                new MineHomeFragment(),
        };

        vwPgHouseHome.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public Fragment getItem(int i) {
                return fragments[i];
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragments[position].getTitle();
            }
        });

        btNvBottomNav.addItem(new BottomNavigationItem(R.drawable.home, "主页"));
        btNvBottomNav.addItem(new BottomNavigationItem(R.drawable.mine, getString(R.string.mine)));
        btNvBottomNav.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                vwPgHouseHome.setCurrentItem(position, true);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        btNvBottomNav.initialise();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //手动调用一下 这里viewpager中的fragment不会回调onActivityResult
        for (BaseFragment frag : fragments) {
            if(frag.isVisibleToUser()){
                frag.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
