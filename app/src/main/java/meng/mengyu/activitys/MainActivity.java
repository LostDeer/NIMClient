package meng.mengyu.activitys;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import meng.mengyu.R;
import meng.mengyu.adapters.FragmentAdapter;
import meng.mengyu.base.BaseActivity;
import meng.mengyu.fragments.ListFragment;
import meng.mengyu.fragments.WeCharFragment;

public class MainActivity extends BaseActivity {
    @BindView(R.id.vp_main)
    ViewPager mVpMain;
    @BindView(R.id.tv_wechar)
    TextView mTvWechar;
    @BindView(R.id.tv_Contactperson)
    TextView mTvContactperson;
    @BindView(R.id.tv_me)
    TextView mTvSetting;
    private List mFragmentList = new ArrayList<>(3);
    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setListener() {
        mFragmentList.add(new WeCharFragment());
        mFragmentList.add(new ListFragment());
        mFragmentList.add(new ListFragment());
        //        mFragmentList.add(conversationListFragment1);
//        mFragmentList.add(conversationListFragment2);
//        mFragmentList.add(FragmentFactory.getInstance().getContactsFragment());
    }

    @Override
    protected void processLogic() {
        mVpMain.setOffscreenPageLimit(2);
        mVpMain.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragmentList));
    }

    @OnClick({R.id.tv_wechar, R.id.tv_Contactperson, R.id.tv_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_wechar:
                mVpMain.setCurrentItem(0);
                break;
            case R.id.tv_Contactperson:
                mVpMain.setCurrentItem(1);
                break;
            case R.id.tv_me:
                mVpMain.setCurrentItem(2);
                break;
        }
    }
}
