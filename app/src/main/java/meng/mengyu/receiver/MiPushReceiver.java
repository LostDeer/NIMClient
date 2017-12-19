package meng.mengyu.receiver;

import android.content.Context;
import android.util.Log;

import com.netease.nimlib.sdk.mixpush.MiPushMessageReceiver;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

/**
 * Created by ${LostDeer} on 2017/12/18.
 * Github:https://github.com/LostDeer
 * /**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 DemoMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、DemoMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、DemoMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、DemoMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、DemoMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、DemoMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 */

public class MiPushReceiver extends MiPushMessageReceiver {
    public MiPushReceiver() {
    }

    /**
     * 消息透穿
     * @param context
     * @param miPushMessage
     */
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        Log.e("MiPushReceiver", "onReceivePassThroughMessage:"+miPushMessage.toString());
    }

    /**
     * 通知栏点击之后
     * @param context
     * @param miPushMessage
     */
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        Log.e("MiPushReceiver", "onNotificationMessageClicked:"+miPushMessage.toString());
    }

    /**
     * 正常通知栏消息
     * @param context
     * @param miPushMessage
     */
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        Log.e("MiPushReceiver", "onNotificationMessageArrived:"+miPushMessage.toString());
    }

    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        Log.e("MiPushReceiver", "onReceiveRegisterResult:"+miPushCommandMessage.toString());
    }

    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        Log.e("MiPushReceiver", "onCommandResult:"+miPushCommandMessage);
    }
}
