package meng.mengyu.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import meng.mengyu.R;

/**
 * Created by ${LostDeer} on 2017/12/21.
 * Github:https://github.com/LostDeer
 */

public class ViewWindow {
    private static LayoutParams mLayoutParams;
    private static WindowManager mWindowManager;
    private static View mView;

    public static void initView(Context context) {
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mLayoutParams = new LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_TOAST, 24, -3);
        mLayoutParams.gravity = 51;
        mView = LayoutInflater.from(context).inflate(R.layout.window_tasks, null);
    }

    public static void showView(Context context, String str) {
        if (mWindowManager == null) {
            initView(context);
        }
        ((TextView) mView.findViewById(R.id.text)).setText(str);
        try {
            mWindowManager.addView(mView, mLayoutParams);
        } catch (Exception e) {
        }
    }

    public static void removeView() {
        try {
            mWindowManager.removeView(mView);
        } catch (Exception e) {
        }
    }
}
