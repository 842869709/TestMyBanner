package com.yangxuydong.mybanner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 创建时间：2020/9/8
 * 编写人：czy_yangxudong
 * 功能描述：
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null) {
                    listener.onTouchDown();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onTouchUp();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private OnViewPagerTouchEvent listener;

    public void setOnViewPagerTouchEventListener(OnViewPagerTouchEvent l){
        listener = l;
    }

    public interface OnViewPagerTouchEvent{
        void onTouchDown();
        void onTouchUp();
    }
}
