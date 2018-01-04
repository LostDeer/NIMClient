package meng.mengyu.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import meng.mengyu.service.NimAccessibilityService;


/**
 * Created by ${LostDeer} on 2017/12/21.
 * Github:https://github.com/LostDeer
 */

public class AccessibilityUtil {
    private static final String TAG = "AccessibilityUtil";
    private static AccessibilityUtil sAccessibilityUtil = null;

    private AccessibilityUtil() {
    }

    public static synchronized AccessibilityUtil getInstance() {
        if (sAccessibilityUtil == null) {
            synchronized (AccessibilityUtil.class) {
                if (sAccessibilityUtil == null) {
                    sAccessibilityUtil = new AccessibilityUtil();
                }
            }
        }
        return sAccessibilityUtil;
    }

    /**
     * 该辅助功能开关是否打开了
     * @param accessibilityServiceName：指定辅助服务名字
     * @param context：上下文
     * @return
     */
    private boolean isAccessibilitySettingsOn(String accessibilityServiceName, Context context) {
        int accessibilityEnable = 0;
        String serviceName = context.getPackageName() + "/" +accessibilityServiceName;
        try {
            accessibilityEnable = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        } catch (Exception e) {
            Log.e(TAG, "get accessibility enable failed, the err:" + e.getMessage());
        }
        if (accessibilityEnable == 1) {
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(serviceName)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        }else {
            Log.d(TAG,"Accessibility service disable");
        }
        return false;
    }

    /**
     * 跳转到系统设置页面开启辅助功能
     * @param accessibilityServiceName：指定辅助服务名字
     * @param context：上下文
     */
    public void openAccessibility(String accessibilityServiceName, Context context){
        if (!isAccessibilitySettingsOn(accessibilityServiceName,context)) {
            Toast.makeText(context, "请打开辅助功能", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else {
            Toast.makeText(context, "辅助功能已启动", Toast.LENGTH_SHORT).show();
            doStartApplicationWithPackageName(NimAccessibilityService.sPackagename,context);
        }
    }

    public void doStartApplicationWithPackageName(String packagename, Context context) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }
}
