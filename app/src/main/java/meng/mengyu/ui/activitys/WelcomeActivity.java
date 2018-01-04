package meng.mengyu.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xiaomi.mipush.sdk.MiPushClient;

import meng.mengyu.R;
import meng.mengyu.app.App;
import meng.mengyu.base.BaseActivity;
import meng.mengyu.config.NimCache;
import meng.mengyu.config.event.UserPreferences;

/**
 * Created by LiChaoBo on 2017/9/6.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void setListener() {
        init();
    }

    @Override
    protected void processLogic() {

    }

    private void init() {
        final LoginInfo info = new LoginInfo(App.account,App.token);
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }

    RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>(){
        @Override
        public void onSuccess(LoginInfo param) {
            NimCache.setAccount(param.getAccount());
            MiPushClient.setAlias(WelcomeActivity.this,App.account,null);
//            MiPushClient.setUserAccount(WelcomeActivity.this,param.getAccount(),null);
//            NimUIKit.getUserInfoProvider().getUserInfoAsync(param.getAccount(), new SimpleCallback() {
//                @Override
//                public void onResult(boolean success, Object result, int code) {
//                    Log.e("WelcomeActivity", result.toString());
//                }
//            });
//            UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(param.getAccount());
//            Log.e("WelcomeActivity", userInfo.getAvatar());
            // 初始化消息提醒配置
//            initNotificationConfig();
            Toast.makeText(WelcomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext,MainActivity.class));
            finish();
        }

        @Override
        public void onFailed(int code) {

        }

        @Override
        public void onException(Throwable exception) {

        }
    };


    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = NimCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    public void login(View view){
//        startActivity(new Intent(this,LoginAcitity.class));
    }

    public void register(View view){
//        startActivity(new Intent(this,RegisterAcitity.class));
    }

}
