package meng.mengyu.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class BaseFragment extends Fragment {
    private View mRootView;
    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = initView(inflater, container);
        mContext = getContext();
        ButterKnife.bind(this, mRootView);//绑定到butterknife
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();
    }

    /**
     * 初始化布局
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View initView(LayoutInflater inflater, ViewGroup container);

    /**
     * 设置监听
     */
    protected abstract void initListener();

    /**
     * 数据加载
     */
    protected abstract void initData();


}
