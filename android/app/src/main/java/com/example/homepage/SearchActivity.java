package com.example.homepage;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.entity.RecipeItemClient;
import com.example.homepage.adapter.RecipeContentAdapter;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText searchContent;
    private Button searchButton;
    private RecipeContentAdapter contentAdapter;
    private int lastVisibleItem = 0;
    private GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
    private String kind = "";
    private int pageCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_fragment);
        ImageView imageView = findViewById(R.id.search_return_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.search_fragment_recycler_view);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        searchContent = findViewById(R.id.search_fragment_content);
        searchContent.setHint("请输入食谱、食材");
        searchContent.setWidth(getWindowManager().getDefaultDisplay().getWidth() - 200);
        searchContent.setMaxWidth(getWindowManager().getDefaultDisplay().getWidth() - 200);
        searchButton = findViewById(R.id.search_fragment_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = searchContent.getText().toString();
                System.out.println("搜索的内容为：" + content);
                pageCount = 1;
                kind = content;
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

        contentAdapter = new RecipeContentAdapter(this, new ArrayList<RecipeItemClient>());
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                //System.out.println("最后一个视图的位置为：" + lastVisibleItem);
            }
        });

    }

    /**
     * 根据关键字进行查询获取食谱
     *
     * @param keyWord
     * @return
     */
    private List<RecipeItemClient> postRequest(String keyWord) {
        String param = "keyWord=" + keyWord + "&pageCount=" + pageCount;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getRecipesByKeyWord", param);
        if (result == null) {
            InternetTooltip.tooltip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        pageCount++;
        return clients;

    }
}
