package meng.mengyu.receiver;

import android.content.Context;

import com.netease.nimlib.sdk.mixpush.MixPushMessageHandler;

import java.util.Map;

/**
 * Created by ${LostDeer} on 2017/12/18.
 * Github:https://github.com/LostDeer
 */

public class PushMessageHandler implements MixPushMessageHandler {
    @Override
    public boolean onNotificationClicked(Context context, Map<String, String> map) {
        return false;
    }

    @Override
    public boolean cleanHuaWeiNotifications() {
        return false;
    }
}
