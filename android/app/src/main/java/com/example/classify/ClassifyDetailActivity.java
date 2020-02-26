package com.example.classify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.entity.ContentItem;
import com.example.entity.RecipeItemClient;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class ClassifyDetailActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private List<ContentItem> contentItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private String kindName;
    private int lastVisibleItem = 0;
    private GridLayoutManager layoutManager;
    private int pageCount = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.classify_detail);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView = findViewById(R.id.classify_detail_recycler_view);
        findViewById(R.id.classify_detail_back_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        kindName = (String) bundle.get("kindName");
        final ClassifyDetailAdapter detailAdapter = new ClassifyDetailAdapter(this, postRequest(kindName));
        recyclerView.setAdapter(detailAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView1, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    //System.out.println("上一次的位置为：" + lastVisibleItem + "  是否消失" + detailAdapter.isFadeTips());
                    if (lastVisibleItem + 1 == detailAdapter.getItemCount()) {
                        System.out.println("执行第一个跟新操作");
                        new Runnable() {
                            @Override
                            public void run() {
                                List<RecipeItemClient> newData = postRequest(kindName);
                                if (newData.size() != 0)
                                    detailAdapter.updateList(newData, true);
                                else {
                                    detailAdapter.updateList(null, false);
                                }
                            }
                        }.run();
                        detailAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                System.out.println("最后一个视图的位置为：" + lastVisibleItem);
            }
        });
    }

    private List<RecipeItemClient> postRequest(String kindName) {

        String param = "kindName=" + kindName + "&pageCount=" + pageCount + "&pageSize=" + PAGE_SIZE;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getRecipesByRecipeKind", param);
        pageCount++;
        if (result == null) {
            InternetTooltip.tooltip(this,"请求出现异常，请检查是否网络未连接");
            return new ArrayList<>();
        }
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        System.out.println("获取的数据个数为：" + clients.size());
        return clients;

    }


}
