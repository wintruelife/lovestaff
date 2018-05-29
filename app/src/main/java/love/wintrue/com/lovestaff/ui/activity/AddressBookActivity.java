package love.wintrue.com.lovestaff.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.base.BaseActivity;
import love.wintrue.com.lovestaff.ui.fragment.AddressBookFragment;
import love.wintrue.com.lovestaff.widget.CustomViewPager;
import love.wintrue.com.lovestaff.widget.actionbar.CommonActionBar;

/**
 * Created by XDHG on 2018/5/28.
 */

public class AddressBookActivity extends BaseActivity {
    @Bind(R.id.title_actionbar_login)
    CommonActionBar cab;
    @Bind(R.id.stl_addressBook_title)
    SlidingTabLayout stlAddressBookTitle;
    @Bind(R.id.vp_addressBook_type)
    CustomViewPager vpAddressBookType;
    private String[] mTitles = {"企业通讯录", "个人通讯录"};
    private List<Fragment> fragmentList;
    private MyPagerAdapter myPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_book;
    }

    @Override
    public void initView() {
        cab.setActionBarTitle("通讯录");
        cab.setLeftImgBtn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragmentList = new ArrayList<>();
        fragmentList.add(AddressBookFragment.instance(0));
        fragmentList.add(AddressBookFragment.instance(1));
        vpAddressBookType.setNoScroll(true);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vpAddressBookType.setAdapter(myPagerAdapter);
        stlAddressBookTitle.setViewPager(vpAddressBookType, mTitles);
        vpAddressBookType.setCurrentItem(0);
        vpAddressBookType.setOffscreenPageLimit(mTitles.length);

        stlAddressBookTitle.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        vpAddressBookType.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
