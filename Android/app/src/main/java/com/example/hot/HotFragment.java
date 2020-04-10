package com.example.hot;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONObject;
import com.example.common.SpacesItemDecoration;
import com.example.R;
import com.example.entity.RecipeItemClient;
import com.example.homepage.adapter.RecipeContentAdapter;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<RecipeItemClient> contents = new ArrayList<>();
    private LinearLayout menuLayout;
    private RecipeContentAdapter viewAdapter;
    private int lastVisibleItem = 0;
    private GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
    private SwipeRefreshLayout refreshLayout;
    private static final String DEFAULT_KIND = "默认";
    private String kindName = "";
    private static int pageCount = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_fregment, container, false);
        recyclerView = view.findViewById(R.id.hot_recycler_view);
        menuLayout = view.findViewById(R.id.hot_menu_item);
        menuLayout.setOrientation(LinearLayout.HORIZONTAL);
        refreshLayout = view.findViewById(R.id.hot_fragment_swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorLightBlue);
        refreshLayout.setOnRefreshListener(this);
        initMenu();
        kindName = DEFAULT_KIND;
        initRecyclerView(kindName);
        return view;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        viewAdapter.resetData();
        updateContent(kindName);
        refreshLayout.setRefreshing(false);
    }

    private void initMenu() {
        String[] menus = {"家常菜", "私家菜", "凉菜", "海鲜", "热菜", "汤粥", "素食", "酱料蘸料", "微波炉", "火锅底料", "甜品点心", "糕点主食", "干果制作", "卤酱", "时尚饮品"};
        for (int i = 0; i < menus.length; i++) {
            final Button button = new Button(this.getActivity());
            button.setText(menus[i]);
            button.setBackgroundColor(getResources().getColor(R.color.colorWhiteSmoke));

            kindName = menus[i];
            final String kind = menus[i];
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("按钮" + kind + "被点击");
                    updateContent(kind);
                }
            });
            menuLayout.addView(button);
        }
    }

    private void initRecyclerView(final String kind) {
        recyclerView.setLayoutManager(layoutManager);
        contents = postRequest(kind);
        viewAdapter = new RecipeContentAdapter(getActivity(), contents);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView1, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem + 1 == viewAdapter.getItemCount()) {
                        new Runnable() {
                            @Override
                            public void run() {
                                List<RecipeItemClient> newData = postRequest(kind);
                                if (newData.size() != 0)
                                    viewAdapter.updateList(newData, true);
                                else {
                                    viewAdapter.updateList(null, false);
                                }
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
                System.out.println("最后一个视图的位置为：" + lastVisibleItem);
            }
        });
    }

    private void updateContent(final String kindName) {
        final String kind = kindName;

        new Runnable() {
            @Override
            public void run() {
                List<RecipeItemClient> clients = postRequest(kind);
                viewAdapter.setContentItems(clients);
                viewAdapter.notifyDataSetChanged();
            }
        }.run();

    }

    /**
     * 根据某种类别随机随机获取十个数据
     */
    private List<RecipeItemClient> postRequest(String kindName) {
        String param = "kind=" + kindName + "&pageCount=" + pageCount;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getRandomRecipesByRecipeKind", param);
        if (result == null) {
            InternetTooltip.tooltip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        return clients;
    }
}
