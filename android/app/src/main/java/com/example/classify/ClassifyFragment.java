package com.example.classify;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.classify.entity.ClassifyViewPager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClassifyFragment extends Fragment {

    private List<String> firstMenuNames;
    private TextView firstMenuTextView[];
    private LayoutInflater inflater;
    private LinearLayout linearLayout;
    private int scrollViewWidth = 0, scrollViewMiddle = 0;
    private int currentItem = 0;
    private ClassifyViewPager secondMenuViewPager;
    private List<View> views = new ArrayList<>();
    private Context context;
    private Button searchButton;

    private LinearLayout contentLayout;
    private Map<String, List<String>> data = new HashMap<>();
    private static final int textViewHeight = 150;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classify_fragment, container, false);
        contentLayout = view.findViewById(R.id.classify_content_layout);
        postRequest();
        Iterator<String> iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            initView(key, data.get(key));
        }
        return view;
    }

    private void initView(String kind, List<String> values) {
        GridLayout valueLayout = new GridLayout(getActivity());
        valueLayout.setColumnCount(2);
        valueLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        for (final String value : values) {
            TextView textView = new TextView(getActivity());
            textView.setText(value);
            textView.setHeight(textViewHeight);
            textView.setWidth(width / 2);
            textView.setBackground(getActivity().getResources().getDrawable(R.drawable.grid_frame));
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HealthDietDetailActivity.class);
                    intent.putExtra("healthDietName", value);
                    startActivity(intent);
                }
            });
            valueLayout.addView(textView);
        }
        TextView textView = new TextView(getActivity());
        textView.setText(kind);
        textView.setTextSize(15);
        textView.setHeight(textViewHeight);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_VERTICAL);

        contentLayout.addView(textView);
        contentLayout.addView(valueLayout);

    }

    private void postRequest() {
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getAllClassifies", "");
        if (result != null) {
            JSONObject object = JSONObject.parseObject(result);
            data = JSONObject.toJavaObject(object, Map.class);
        } else {
            /**请求出现问题*/
            InternetTooltip.tooltip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
        }
    }
}