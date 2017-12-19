package meng.mengyu.ui.activitys;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import meng.mengyu.R;
import meng.mengyu.base.BaseActivity;
import meng.mengyu.ui.adapters.FragmentAdapter;
import meng.mengyu.ui.fragments.ListFragment;
import meng.mengyu.ui.fragments.WeCharFragment;

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
        initUnreadCover();
    }

    private void initUnreadCover() {
        DropManager.getInstance().init(mContext, (DropCover) findView(R.id.unread_cover),
                new DropCover.IDropCompletedListener() {
                    @Override
                    public void onCompleted(Object id, boolean explosive) {
                        if (id == null || !explosive) {
                            return;
                        }
                        if (id instanceof RecentContact) {
                            RecentContact r = (RecentContact) id;
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                            LogUtil.i("HomeFragment", "clearUnreadCount, sessionId=" + r.getContactId());
                        } else if (id instanceof String) {
                            if (((String) id).contentEquals("0")) {
                                NIMClient.getService(MsgService.class).clearAllUnreadCount();
                                LogUtil.i("HomeFragment", "clearAllUnreadCount");
                            } else if (((String) id).contentEquals("1")) {
                                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
                                LogUtil.i("HomeFragment", "clearAllSystemUnreadCount");
                            }
                        }
                    }
                });
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
