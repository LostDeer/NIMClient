package meng.mengyu.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.util.NIMUtil;

import meng.mengyu.config.NimCache;
import meng.mengyu.config.NimSDKOptionConfig;
import meng.mengyu.config.event.DemoOnlineStateContentProvider;
import meng.mengyu.receiver.PushMessageHandler;
import meng.mengyu.session.SessionHelper;

/**
 * Created by ${LostDeer} on 2017/12/13.
 * Github:https://github.com/LostDeer
 */

public class App extends Application {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        NimCache.setContext(this);
        // 注册小米推送，参数：小米推送证书名称（需要在云信管理后台配置）、appID 、appKey，该逻辑放在 NIMClient init 之前
        NIMPushClient.registerMiPush(this, "mengyu", "2882303761517683311", "5301768378311");

        NIMPushClient.registerMixPushMessageHandler(new PushMessageHandler());
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), NimSDKOptionConfig.getSDKOptions(this));
        // ... your codes
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            NimUIKit.init(this, buildUIKitOptions());
            // 可选定制项
            // 注册定位信息提供者类（可选）,如果需要发送地理位置消息，必须提供。
            // demo中使用高德地图实现了该提供者，开发者可以根据自身需求，选用高德，百度，google等任意第三方地图和定位SDK。
//            NimUIKit.setLocationProvider(new NimDemoLocationProvider());

            // 会话窗口的定制: 示例代码可详见demo源码中的SessionHelper类。
            // 1.注册自定义消息附件解析器（可选）
            // 2.注册各种扩展消息类型的显示ViewHolder（可选）
            // 3.设置会话中点击事件响应处理（一般需要）
            SessionHelper.init();

            // 通讯录列表定制：示例代码可详见demo源码中的ContactHelper类。
            // 1.定制通讯录列表中点击事响应处理（一般需要，UIKit 提供默认实现为点击进入聊天界面)
//            ContactHelper.init();

            // 在线状态定制初始化。
            NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
            // 2、相关Service调用

        }
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        //开启@功能
        options.aitEnable=false;
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    //掉线重连的时候会用到
    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
//        String account = "lostdeer";
        String account = "LostDeer001";
        String token = "075c375022cc859132234e6343b82dd4";
//        String token = "03dd100696cfe52a3defb8ee661373a2";
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            NimCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

}
