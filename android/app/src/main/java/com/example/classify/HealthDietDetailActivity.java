package com.example.classify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.classify.adapter.HealthDietDetailAdapter;
import com.example.entity.RecipeItemClient;
import com.example.entity.HealthDietClient;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class HealthDietDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<RecipeItemClient> recipeItemClients;
    private LinearLayoutManager linearLayout;
    private static int lastVisibleItem = 0;
    private String healthDietName;
    private int pageCount = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.health_diet);
        linearLayout = new GridLayoutManager(this, 1);
        recyclerView = findViewById(R.id.health_diet_recipes_recycler);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        healthDietName = (String) bundle.get("healthDietName");
        TextView textView = findViewById(R.id.health_diet_detail_content);
        textView.setText(healthDietName + "详情");
        findViewById(R.id.health_diet_detail_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final HealthDietDetailAdapter detailAdapter = new HealthDietDetailAdapter(this, postRequest(healthDietName), getData(healthDietName));
        recyclerView.setAdapter(detailAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.addItemDecoration(new DividerItemDecoration(HealthDietDetailActivity.this, DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(linearLayout);

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
                                List<RecipeItemClient> newData = postRequest(healthDietName);
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
                lastVisibleItem = linearLayout.findLastCompletelyVisibleItemPosition();
            }
        });
    }


    private List<RecipeItemClient> postRequest(String kindName) {

        String param = "healthDietName=" + kindName + "&pageCount=" + pageCount + "&pageSize=" + PAGE_SIZE;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getRecipesByHealthDiet", param);
        pageCount++;
        if (result == null) {
            InternetTooltip.tooltip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        System.out.println("获取的数据个数为：" + clients.size());
        return clients;

    }


    /**
     * 初始化数据
     */
    private HealthDietClient getData(String healthDietName) {
        String param = "healthDietName=" + healthDietName;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getDietByHealthDietName", param);
        if (result == null) {
            InternetTooltip.tooltip(HealthDietDetailActivity.this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return null;
        }
        JSONObject object = JSONObject.parseObject(result);
        HealthDietClient healthDiet = JSONObject.toJavaObject(object, HealthDietClient.class);
        return healthDiet;
    }

}
