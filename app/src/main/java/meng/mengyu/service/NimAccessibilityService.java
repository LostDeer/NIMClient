package meng.mengyu.service;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import meng.mengyu.utils.AccessibilityUtil;
import meng.mengyu.widget.ViewWindow;

/**
 * Created by ${LostDeer} on 2017/12/21.
 * Github:https://github.com/LostDeer
 */

public class NimAccessibilityService extends BaseAccessibilityService {

    private AccessibilityNodeInfo mNodeInfo;
    public static String sPackagename = "com.guoyidaji.zhongyigu";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show();
        AccessibilityUtil.getInstance().doStartApplicationWithPackageName(sPackagename, this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        Log.e("NimAccessibilityService", packageName);
        String className = event.getClassName().toString();

        boolean startsWith = className.toString().startsWith(packageName);

        if (startsWith) {
            ViewWindow.showView(this, packageName + "\n" + event.getClassName());
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                event.getPackageName().equals(sPackagename)) {
            Log.e("NimAccessibilityService", className);
            //            if(className.equals()){
            //
            //            } else if(className.equals(login)){//登录输入账号密码
            //                mNodeInfo = findViewByID("com.alibaba.android.rimet:id/et_phone_input");
            //                if(mNodeInfo!=null){
            //                    inputText(this, mNodeInfo, nuerName);
            //                    mNodeInfo = findViewByID("com.alibaba.android.rimet:id/et_pwd_login");
            //                    if(mNodeInfo!=null){
            //                        inputText(this, mNodeInfo, pwd);
            //                        SystemClock.sleep(1000);
            //                        mNodeInfo = findViewByID("com.alibaba.android.rimet:id/btn_next");
            //                        performViewClick(mNodeInfo);
            //                    }
            //                }
            //            }
            mNodeInfo = findViewByID("com.guoyidaji.zhongyigu:id/ll_mainMe");
            if (mNodeInfo != null) {
                SystemClock.sleep(1000);
                performViewClick(mNodeInfo);
                mNodeInfo = findViewByID("com.guoyidaji.zhongyigu:id/rl_meBalance");
                if (mNodeInfo != null) {
                    performViewClick(mNodeInfo);
                    SystemClock.sleep(1000);
                    mNodeInfo = findViewByID("com.guoyidaji.zhongyigu:id/rl_Transactionrecord");
                    if (mNodeInfo != null) {
                        performViewClick(mNodeInfo);
                        SystemClock.sleep(1000);
                        mNodeInfo = findViewByID("com.guoyidaji.zhongyigu:id/et_Name");
                        if (mNodeInfo != null) {
                            inputText(this, mNodeInfo, "LostDeer");
                        }
                    }
                }
            }
        }
    }

    //这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用多次。
    @Override
    public void onInterrupt() {
    }

    //在系统将要关闭这个AccessibilityService会被调用。在这个方法中进行一些释放资源的工作。
    @Override
    public boolean onUnbind(Intent intent) {
        ViewWindow.removeView();
        return super.onUnbind(intent);
    }
}
