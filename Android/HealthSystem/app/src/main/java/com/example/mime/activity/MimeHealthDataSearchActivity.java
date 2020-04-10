package com.example.mime.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.example.entity.RecipeItemClient;
import com.example.mime.HealthDataRecipeContentAdapter;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class MimeHealthDataSearchActivity extends AppCompatActivity {

    private ImageView backImage;
    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView searchRecyclerView;
    private RecyclerView addRecyclerView;
    private Button submitButton;
    private int pageCount;
    private HealthDataRecipeContentAdapter contentAdapter;
    private HealthDataRecipeContentAdapter addContentAdapter;
    private int lastVisibleItem = 0;
    private GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
    private String kind = "";
    private LocalBroadcastManager broadcastManager;
    private BroadCastReceiver broadCastReceiver;
    private List<RecipeItemClient> recipeItemClients = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_health_data_search_fragment);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadCastReceiver = new BroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("AddRecipeContentAction");
        intentFilter.addAction("DeleteRecipeContentAction");
        broadcastManager.registerReceiver(broadCastReceiver, intentFilter);
        addRecyclerView = findViewById(R.id.mime_health_data_add_content_recycler);
        searchEditText = findViewById(R.id.mime_health_data_search_content);
        searchButton = findViewById(R.id.mime_health_data_search_button);
        searchRecyclerView = findViewById(R.id.mime_health_data_recycler_view);
        findViewById(R.id.mime_health_data_return_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = searchEditText.getText().toString();
                System.out.println("搜索的内容为：" + content);
                if (content.isEmpty()) {
                    InternetTooltip.tip(MimeHealthDataSearchActivity.this, "请输入关键字");
                    return;
                }
                pageCount = 1;
                new Runnable() {
                    @Override
                    public void run() {
                        List<RecipeItemClient> clients = postRequest(content);
                        contentAdapter.setContentItems(clients);
                        contentAdapter.notifyDataSetChanged();
                    }
                }.run();
            }
        });
        contentAdapter = new HealthDataRecipeContentAdapter(this, new ArrayList<RecipeItemClient>(), 1);
        searchRecyclerView.setAdapter(contentAdapter);
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.addItemDecoration(new SpacesItemDecoration());
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView1, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem + 1 == contentAdapter.getItemCount()) {
                        new Runnable() {
                            @Override
                            public void run() {
                                List<RecipeItemClient> newData = postRequest(kind);
                                if (newData.size() != 0)
                                    contentAdapter.updateList(newData, true);
                                else {
                                    contentAdapter.updateList(null, false);
                                }
                            }
                        }.run();
                        contentAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

        addContentAdapter = new HealthDataRecipeContentAdapter(this, new ArrayList<RecipeItemClient>(), 2);
        addRecyclerView.setAdapter(addContentAdapter);
        addRecyclerView.setItemAnimator(new DefaultItemAnimator());
        addRecyclerView.addItemDecoration(new SpacesItemDecoration());
        addRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        addRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        findViewById(R.id.mime_health_data_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeItemClients.size() == 0) {
                    InternetTooltip.tip(MimeHealthDataSearchActivity.this, "您未添加食谱，请先添加食谱");
                    return;
                }
                Intent intent = new Intent(MimeHealthDataSearchActivity.this, MimeHealthDataContentActivity.class);
                intent.putExtra("recipes", JSONObject.toJSONString(recipeItemClients));
                startActivity(intent);
                finish();
            }
        });


    }

    private List<RecipeItemClient> postRequest(String keyWord) {
        String param = "keyWord=" + keyWord + "&pageCount=" + pageCount;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getRecipesByKeyWord", param);
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        pageCount++;
        return clients;
    }

    private class BroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("AddRecipeContentAction")) {
                System.out.println("接收到提交的按钮");
                String recipeNum = (String) intent.getExtras().get("recipeNum");
                for (RecipeItemClient itemClient : recipeItemClients) {
                    if (itemClient.getNum().equals(recipeNum)) {
                        InternetTooltip.tip(MimeHealthDataSearchActivity.this, "食谱已添加");
                        return;
                    }
                }

                String recipeName = (String) intent.getExtras().get("recipeName");
                String recipeAuthor = (String) intent.getExtras().get("recipeAuthor");
                byte[] image = intent.getExtras().getByteArray("recipeImage");
                String recipeKind = (String) intent.getExtras().get("recipeKind");
                int collectCount = (int) intent.getExtras().get("recipeCollectCount");
                int viewCount = (int) intent.getExtras().get("recipeViewCount");

                RecipeItemClient recipeItemClient = new RecipeItemClient(recipeNum, recipeName, image, recipeKind, viewCount, collectCount, recipeAuthor);
                recipeItemClients.add(recipeItemClient);
                addContentAdapter.updateContent(recipeItemClients);
                addContentAdapter.notifyDataSetChanged();
            }
            if (intent.getAction().equals("DeleteRecipeContentAction")) {
                String recipeNum = (String) intent.getExtras().get("recipeNum");
                try {
                    int num = 0;
                    for (int i = 0; i < recipeItemClients.size(); i++) {
                        if (recipeItemClients.get(i).getNum().equals(recipeNum)) {
                            num = i;
                            break;
                        }
                    }
                    recipeItemClients.remove(num);
                    addContentAdapter.updateContent(recipeItemClients);
                    addContentAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    InternetTooltip.tip(MimeHealthDataSearchActivity.this, "删除食谱异常");
                }
            }
        }

    }

}
