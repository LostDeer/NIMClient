package meng.mengyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

/**
 * Created by ${LostDeer} on 2017/12/26.
 * Github:https://github.com/LostDeer
 */

public class CustomScrollView extends ScrollView {
    private OnScrollChangeListener mOnScrollChangeListener;

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    /**
     * 设置滚动接口
     * @param
     */

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     *定义一个滚动接口
     * */

    public interface OnScrollChangeListener{
        void onScrollChanged(CustomScrollView scrollView,int l, int t, int oldl, int oldt);
    }

    /**
     * 当scrollView滑动时系统会调用该方法,并将该回调放过中的参数传递到自定义接口的回调方法中,
     * 达到scrollView滑动监听的效果
     *
     * */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangeListener!=null){
            mOnScrollChangeListener.onScrollChanged(this,l,t,oldl,oldt);
            if (oldt < t && ((t - oldt) > 15)) {// 向上
                Log.e("wangly", "距离："+(oldt < t) +"---"+(t - oldt));
                Log.e("TAG","向上滑动");

            }  else if (oldt > t && (oldt - t) > 15) {// 向下
                Log.e("wangly", "距离："+(oldt > t) +"---"+(oldt - t));
                Log.e("TAG"," 向下滑动");
            }
        }
    }
}
