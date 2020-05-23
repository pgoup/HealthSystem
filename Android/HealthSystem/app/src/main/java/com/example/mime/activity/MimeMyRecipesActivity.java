package com.example.mime.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.entity.RecipeItemClient;
import com.example.entity.UserInfoClient;
import com.example.homepage.adapter.RecipeContentAdapter;
import com.example.mime.HealthDataRecipeContentAdapter;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class MimeMyRecipesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private HealthDataRecipeContentAdapter viewAdapter;
    private List<RecipeItemClient> contents = new ArrayList<>();
    private int lastVisibleItem = 0;
    private GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
    private int pageCount = 0;
    private LocalBroadcastManager broadcastManager;
    private BroadCastReceiver broadCastReceiver;
    private SwipeRefreshLayout refreshLayout;
    private static int status = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_related);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadCastReceiver = new BroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DeleteUserRecipeAction");
        broadcastManager.registerReceiver(broadCastReceiver, intentFilter);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        pageCount = Integer.parseInt(bundle.get("pageCount").toString());
        contents = postRequest();
        TextView textView = findViewById(R.id.mime_related_text);
        textView.setText("我的食谱");
        findViewById(R.id.mime_related_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout = findViewById(R.id.mime_collect_swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorLightBlue);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.mime_related_recycler_view);
        viewAdapter = new HealthDataRecipeContentAdapter(this, contents, 3);
        System.out.println("进入系统后的个数为：" + viewAdapter.getContentItems().size());
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.addItemDecoration(new DividerItemDecoration(MimeMyRecipesActivity.this, DividerItemDecoration.VERTICAL));

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
                                List<RecipeItemClient> newData = postRequest();
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

    @Override
    protected void onDestroy() {

        if (broadCastReceiver != null) {
            broadcastManager.unregisterReceiver(broadCastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        viewAdapter.resetData();
        updateContent();
        refreshLayout.setRefreshing(false);
    }

    private void updateContent() {
        new Runnable() {
            @Override
            public void run() {
                pageCount = 1;
                List<RecipeItemClient> clients = postRequest();
                viewAdapter.setContentItems(clients);
                viewAdapter.notifyDataSetChanged();
            }
        }.run();

    }

    /**
     * 获取用户收藏的食谱
     */
    private List<RecipeItemClient> postRequest() {
        String param = "pageCount=" + pageCount + "&userAccount=" + UserManager.getInstance().getUserInfo(MimeMyRecipesActivity.this).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getMimeRecipesByUserAccount", param);
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        pageCount++;
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        return clients;
    }

    private void deleteRecipe(String recipeNum, List<RecipeItemClient> clients) {
        String param = "recipeNum=" + recipeNum;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "deleteRecipe", param);
        System.out.println("result:" + result);
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        if (result.equals("OK")) {
            try {
                System.out.println("数据量为:" + clients.size());
                if (clients.size() == 0) return;
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).getNum().equals(recipeNum)) {
                        clients.remove(i);
                        System.out.println("删除后的数据量为：" + clients.size());
                        viewAdapter.setContentItems(clients);
                        viewAdapter.notifyDataSetChanged();
                        return;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                InternetTooltip.tip(MimeMyRecipesActivity.this, "删除食谱异常");
            }
        } else
            InternetTooltip.tip(MimeMyRecipesActivity.this, "删除失败！");


    }

    private class BroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DeleteUserRecipeAction")) {
                System.out.println("状态为：" + status);
                status++;
                String recipeNum = (String) intent.getExtras().get("recipeNum");
                List<RecipeItemClient> clients = JSONObject.parseArray((String) intent.getExtras().get("contents"), RecipeItemClient.class);

                deleteRecipe(recipeNum, clients);
            }
        }

    }
}