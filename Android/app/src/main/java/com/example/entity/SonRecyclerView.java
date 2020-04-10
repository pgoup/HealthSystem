package com.example.entity;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SonRecyclerView extends RecyclerView {

    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;
    private float mLastY = 0;//记录上次y位置
    private boolean isTopToButton = false;
    private boolean isBottomToTop = false;
    private CallBack callback;

    public CallBack getCallback() {
        return callback;
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public interface CallBack {
        void moveEvent();
    }

    public SonRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    private void isIntercept(float nowY) {
        isTopToButton = false;
        isTopToButton = false;
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //得到当前界面，最后一个子视图对应的position
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            //得到当前界面，第一个子视图的position
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        //得到当前界面课件数的大小
        int visibleItemCount = layoutManager.getChildCount();
        //得到recyclerview对应所有数据的大小
        int totalItemCount = layoutManager.getItemCount();

    }
}
