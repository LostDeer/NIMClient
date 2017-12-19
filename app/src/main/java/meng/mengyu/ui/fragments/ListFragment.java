package meng.mengyu.ui.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import meng.mengyu.R;
import meng.mengyu.base.BaseFragment;

/**
 * Created by ${LostDeer} on 2017/12/13.
 * Github:https://github.com/LostDeer
 */

public class ListFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {

    }
}
