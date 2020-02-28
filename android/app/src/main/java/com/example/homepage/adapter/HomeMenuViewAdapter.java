package com.example.homepage.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

public class HomeMenuViewAdapter extends PagerAdapter {

    private List<View> views;

    public HomeMenuViewAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views == null ? 0 : views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}