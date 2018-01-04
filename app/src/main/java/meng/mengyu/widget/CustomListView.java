package meng.mengyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by ${LostDeer} on 2017/12/27.
 * Github:https://github.com/LostDeer
 */

public class CustomListView extends ListView {
    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
