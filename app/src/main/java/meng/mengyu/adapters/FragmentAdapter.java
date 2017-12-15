package meng.mengyu.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import meng.mengyu.base.BaseFragment;


/**
 * Created by LiChaoBo on 2017/9/28.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragments;
    public FragmentAdapter(FragmentManager manager, List<BaseFragment> fragmentList) {
        super(manager);
        this.mFragments = fragmentList;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
