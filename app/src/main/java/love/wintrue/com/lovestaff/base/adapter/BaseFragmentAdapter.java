package love.wintrue.com.lovestaff.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lhe on 2017/8/23.
 */

public class BaseFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> list;

    public BaseFragmentAdapter(FragmentManager fm, List<T> list){
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }
}
