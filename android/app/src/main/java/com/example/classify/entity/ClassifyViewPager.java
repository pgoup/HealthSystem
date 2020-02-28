package com.example.classify.entity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ClassifyViewPager extends ViewPager {

    private boolean scrollable = false;

    public ClassifyViewPager(Context context) {
        super(context);
    }

    public ClassifyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable ? super.onInterceptTouchEvent(ev) : scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scrollable ? super.onTouchEvent(ev) : scrollable;
    }

    /**
     * 设置无切换效果
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
