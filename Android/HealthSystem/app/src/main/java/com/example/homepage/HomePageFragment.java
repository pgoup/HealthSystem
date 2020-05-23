package com.example.homepage;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.entity.HomePageMenu;
import com.example.entity.RecipeItemClient;
import com.example.homepage.adapter.HomePageMainAdapter;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public RecyclerView recyclerRootView;
    private Button searchButton;
    private GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
    private int lastVisibleItem = 0;
    private HomePageMainAdapter mainAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.home_page_fragment, container, false);
        recyclerRootView = view.findViewById(R.id.home_page_recycler_root_view);
        searchButton = view.findViewById(R.id.home_page_search_button);
        SpannableString spannableString = new SpannableString("1请输入食谱、食材");
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        int b = (int) searchButton.getTextSize();
        drawable.setBounds(10, 0, b, b);
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        searchButton.setText(spannableString);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("searchKind", "1");
                startActivity(intent);
            }
        });
        refreshLayout = view.findViewById(R.id.homepage_swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorLightBlue);
        refreshLayout.setOnRefreshListener(this);
        recyclerRootView.setLayoutManager(layoutManager);
        recyclerRootView.addItemDecoration(new SpacesItemDecoration());
        recyclerRootView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mainAdapter = new HomePageMainAdapter(getActivity(), postRequest(), initMenu());
        recyclerRootView.setAdapter(mainAdapter);
        recyclerRootView.setItemAnimator(new DefaultItemAnimator());
        recyclerRootView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView1, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mainAdapter.isFadeTips() == false && lastVisibleItem + 1 == mainAdapter.getItemCount()) {
                        System.out.println("执行第一个跟新操作");
                        new Runnable() {
                            @Override
                            public void run() {
                                List<RecipeItemClient> newData = postRequest();
                                if (newData.size() != 0)
                                    mainAdapter.updateList(newData, true);
                                else {
                                    mainAdapter.updateList(null, false);
                                }
                            }
                        }.run();
                        mainAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            }
        });
        return view;
    }

    @Override
    public void onRefresh() {
        postRequest();
        mainAdapter.setContents(postRequest());
        mainAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    private List<HomePageMenu> initMenu() {
        List<HomePageMenu> menus = new ArrayList<>();
        menus.add(new HomePageMenu("家常菜", R.drawable.home_page_menu_jiachangcai));
        menus.add(new HomePageMenu("私家菜", R.drawable.home_page_menu_xiangcai));
        menus.add(new HomePageMenu("热菜", R.drawable.home_page_menu_dongbeicai));
        menus.add(new HomePageMenu("凉菜", R.drawable.home_page_menu_qingzhencai));
        menus.add(new HomePageMenu("早餐", R.drawable.home_page_menu_hubeicai));
        menus.add(new HomePageMenu("午餐", R.drawable.home_page_menu_zhecai));
        menus.add(new HomePageMenu("晚餐", R.drawable.home_page_menu_yucai));
        menus.add(new HomePageMenu("全部", R.drawable.menu_more));
        return menus;
    }

    private List<RecipeItemClient> postRequest() {
        String userAccount = "";
        if (UserManager.getInstance().hasUserInfo(getActivity()))
            userAccount = UserManager.getInstance().getUserInfo(getActivity()).getAccount();
        String param = "userAccount=" + userAccount;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getHomePageRecommendedRecipe", param);
        if (result == null) {
            InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        return clients;
    }
}



