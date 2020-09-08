package com.yangxuydong.mybanner;

import android.content.Context;
import android.widget.Scroller;

/**
 * 创建时间：2020/9/8
 * 编写人：czy_yangxudong
 * 功能描述：
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 300;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, android.view.animation.Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int time) {
        mDuration = time;
    }

    public int getmDuration() {
        return mDuration;
    }
}
