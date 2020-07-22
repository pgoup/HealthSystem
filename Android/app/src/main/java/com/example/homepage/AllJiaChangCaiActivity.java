package com.example.homepage;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.entity.RecipePicAndNameClient;
import com.example.homepage.adapter.AllCommonRecipeRecyclerAdapter;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class AllJiaChangCaiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<RecipePicAndNameClient> recipePicAndNameClients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.homepage_all_common_recipe);
        findViewById(R.id.homepage_all_common_recipe_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        postRequest();
        recyclerView = findViewById(R.id.homepage_all_common_recipe_recycler);
        AllCommonRecipeRecyclerAdapter commonRecipeRecyclerAdapter = new AllCommonRecipeRecyclerAdapter(this, recipePicAndNameClients);
        recyclerView.setAdapter(commonRecipeRecyclerAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
    }

    private void postRequest() {
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getAllCommonRecipeKind", "");
        if (result == null) {
            InternetTooltip.tip(AllJiaChangCaiActivity.this, "请求出现异常，请检查是否网络未连接");
            return;
        }
        recipePicAndNameClients = JSONObject.parseArray(result, RecipePicAndNameClient.class);
        if (recipePicAndNameClients == null)
            recipePicAndNameClients = new ArrayList<>();
        return;
    }
}
