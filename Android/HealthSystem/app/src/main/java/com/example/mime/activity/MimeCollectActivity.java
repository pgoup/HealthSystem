package com.example.mime.activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.homepage.adapter.RecipeContentAdapter;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import java.util.ArrayList;
import java.util.List;
public class MimeCollectActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private RecipeContentAdapter viewAdapter;
    private List<RecipeItemClient> contents;
    private int lastVisibleItem = 0;
    private GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
    private int pageCount = 0;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_related);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        pageCount = Integer.parseInt(bundle.get("pageCount").toString());
        contents = postRequest();
        TextView textView = findViewById(R.id.mime_related_text);
        textView.setText("我的收藏");
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
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.addItemDecoration(new DividerItemDecoration(MimeCollectActivity.this, DividerItemDecoration.VERTICAL));
        viewAdapter = new RecipeContentAdapter(this, contents, 1);
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
                pageCount=1;
                List<RecipeItemClient> clients = postRequest();
                viewAdapter.setContentItems(clients);
                viewAdapter.notifyDataSetChanged();
            }
        }.run();

    }
    private List<RecipeItemClient> postRequest() {
        String param = "pageCount=" + pageCount + "&userAccount=" + UserManager.getInstance().getUserInfo(MimeCollectActivity.this).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getCollectedRecipesByUserAccount", param);
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        pageCount++;
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        return clients;
    }
}
