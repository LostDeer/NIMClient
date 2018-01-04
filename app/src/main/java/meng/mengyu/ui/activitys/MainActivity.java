package meng.mengyu.ui.activitys;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import meng.mengyu.R;
import meng.mengyu.base.BaseActivity;
import meng.mengyu.session.reminder.ReminderItem;
import meng.mengyu.session.reminder.ReminderManager;
import meng.mengyu.session.reminder.ReminderSettings;
import meng.mengyu.session.reminder.SystemMessageUnreadManager;
import meng.mengyu.ui.adapters.FragmentAdapter;
import meng.mengyu.ui.fragments.ContactsFragment;
import meng.mengyu.ui.fragments.ListFragment;

public class MainActivity extends BaseActivity implements ReminderManager.UnreadNumChangedCallback {
    @BindView(R.id.vp_main)
    ViewPager mVpMain;
    @BindView(R.id.tv_wechar)
    TextView mTvWechar;
    @BindView(R.id.tv_Contactperson)
    TextView mTvContactperson;
    @BindView(R.id.tv_me)
    TextView mTvSetting;
    @BindView(R.id.tab_new_msg_label)
    DropFake mDropFake;
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
        mFragmentList.add(new ContactsFragment());
        mFragmentList.add(new ListFragment());
        mFragmentList.add(new ContactsFragment());

        //        mFragmentList.add(conversationListFragment1);
//        mFragmentList.add(conversationListFragment2);
//        mFragmentList.add(FragmentFactory.getInstance().getContactsFragment());
    }

    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }
    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };
    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    @Override
    protected void processLogic() {
        mVpMain.setOffscreenPageLimit(2);
        mVpMain.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragmentList));
        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        initUnreadCover();
        mDropFake.setTouchListener(new DropFake.ITouchListener() {
            @Override
            public void onDown() {
                DropManager.getInstance().setCurrentId(String.valueOf(0));
                DropManager.getInstance().down(mDropFake, mDropFake.getText());
            }

            @Override
            public void onMove(float curX, float curY) {
                DropManager.getInstance().move(curX, curY);
            }

            @Override
            public void onUp() {
                DropManager.getInstance().up();
            }
        });
//        AccessibilityUtil.getInstance().openAccessibility("meng.mengyu.service.NimAccessibilityService",this);
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

    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        int unread = item.unread();
        mDropFake.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
        if (unread > 0) {
            mDropFake.setText(String.valueOf(ReminderSettings.unreadMessageShowRule(unread)));
        }
    }

}
