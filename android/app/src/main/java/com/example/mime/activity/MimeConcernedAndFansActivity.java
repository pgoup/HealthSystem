package com.example.mime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.entity.UserInfoClient;
import com.example.mime.MimeConcernFanAdapter;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class MimeConcernedAndFansActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private MimeConcernFanAdapter viewAdapter;
    private List<UserInfoClient> contents;
    private int lastVisibleItem = 0;
    private GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
    private int pageCount = 0;
    private String activityKind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        activityKind = bundle.get("activityKind").toString();
        setContentView(R.layout.mime_related);
        String title = activityKind.equals("concern") ? "我的关注" : "我的粉丝";
        textView = findViewById(R.id.mime_related_text);
        recyclerView = findViewById(R.id.mime_related_recycler_view);
        findViewById(R.id.mime_related_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView.setText(title);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.addItemDecoration(new DividerItemDecoration(MimeConcernedAndFansActivity.this, DividerItemDecoration.VERTICAL));
        viewAdapter = new MimeConcernFanAdapter(this, postRequest());
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView1, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem + 1 == viewAdapter.getItemCount()) {
                        new Runnable() {
                            @Override
                            public void run() {
                                List<UserInfoClient> newData = postRequest();
                                if (newData.size() != 0)
                                    viewAdapter.updateList(newData, true);
                                else
                                    viewAdapter.updateList(null, false);
                            }
                        }.run();
                        viewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

    }

    private List<UserInfoClient> postRequest() {
        String param = "userAccount=" + UserManager.getInstance().getUserInfo(MimeConcernedAndFansActivity.this).getAccount() + "&pageCount=" + pageCount + "&kind=" + activityKind;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getUserConcernedFans", param);
        if (result == null) {
            InternetTooltip.tooltip(MimeConcernedAndFansActivity.this, "请求出现异常，请检查是否网络未连接");
            return new ArrayList<>();
        }
        pageCount++;
        contents = JSONObject.parseArray(result, UserInfoClient.class);
        return contents;
    }
}
