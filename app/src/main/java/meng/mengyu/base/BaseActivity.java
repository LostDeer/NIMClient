package meng.mengyu.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.netease.nim.uikit.common.fragment.TFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected Context mContext;
    private String mClassName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getActivityContext();
        initView();
        ButterKnife.bind(this);
        setListener();
        processLogic();
//        EventBus.getDefault().register(this);
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private void initView() {
        loadViewLayout();
    }

    /**
     * 获取context
     *
     * @return
     */
    protected abstract Context getActivityContext();

    /**
     * 初始化布局
     */
    protected abstract void loadViewLayout();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 加载数据
     */
    protected abstract void processLogic();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * fragment management
     */
    public TFragment addFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<TFragment>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }

    public List<TFragment> addFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }
}

