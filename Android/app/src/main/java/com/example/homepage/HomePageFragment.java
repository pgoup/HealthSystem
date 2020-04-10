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

public class HomePageFragment extends Fragment {

    /**
     * 显示各个食物的view
     */
    public RecyclerView recyclerRootView;
    private SearchView searchView;
    private Button searchButton;
    private GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
    private int lastVisibleItem = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private HomePageMainAdapter mainAdapter;
    private static final int PAGE_SIZE = 10;
    private static int pageCount = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        System.out.println("构建首页页面");
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.home_page_fragment, container, false);
        recyclerRootView = view.findViewById(R.id.home_page_recycler_root_view);
        searchButton = view.findViewById(R.id.home_page_search_button);
        SpannableString spannableString = new SpannableString("1请输入搜索的内容");
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
                startActivity(intent);
            }
        });

        recyclerRootView.setLayoutManager(layoutManager);
        recyclerRootView.addItemDecoration(new SpacesItemDecoration());
        recyclerRootView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mainAdapter = new HomePageMainAdapter(getActivity(), postRequest(), initMenu());
        recyclerRootView.setAdapter(mainAdapter);
        recyclerRootView.setItemAnimator(new DefaultItemAnimator());
        recyclerRootView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView1, int newState) {

                // recyclerRootView.requestDisallowInterceptTouchEvent(true);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    //System.out.println("上一次的位置为：" + lastVisibleItem + "  是否消失" + mainAdapter.isFadeTips());
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


    /**
     * 菜单直接进行初始化
     */
    private List<HomePageMenu> initMenu() {
        List<HomePageMenu> menus = new ArrayList<>();
        menus.add(new HomePageMenu("家常菜", R.drawable.home_page_menu_jiachangcai));
        menus.add(new HomePageMenu("湘菜", R.drawable.home_page_menu_xiangcai));
        menus.add(new HomePageMenu("东北菜", R.drawable.home_page_menu_dongbeicai));
        menus.add(new HomePageMenu("清真菜", R.drawable.home_page_menu_qingzhencai));
        menus.add(new HomePageMenu("湖北菜", R.drawable.home_page_menu_hubeicai));
        menus.add(new HomePageMenu("浙菜", R.drawable.home_page_menu_zhecai));
        menus.add(new HomePageMenu("豫菜", R.drawable.home_page_menu_yucai));
        menus.add(new HomePageMenu("京菜", R.drawable.home_page_menu_jingcai));
        menus.add(new HomePageMenu("韩国料理", R.drawable.home_page_menu_hanguoliaoli));
        menus.add(new HomePageMenu("日本料理", R.drawable.home_page_menu_ribenliaoli));
        menus.add(new HomePageMenu("凉菜", R.drawable.home_page_menu_liangcai));
        return menus;
    }


    /**
     * 发送请求服务器
     */
    private List<RecipeItemClient> postRequest() {
        String userAccount = "";
        if (UserManager.getInstance().hasUserInfo(getActivity()))
            userAccount = UserManager.getInstance().getUserInfo(getActivity()).getAccount().toString();
        String param = "pageCount=" + pageCount + "&userAccount=" + userAccount;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getHomePageRecommendedRecipe", param);
        pageCount++;
        if (result == null) {
            InternetTooltip.tooltip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
            return new ArrayList<>();
        }
        List<RecipeItemClient> clients = JSONObject.parseArray(result, RecipeItemClient.class);
        System.out.println("获取的数据个数为：" + clients.size());
        return clients;
    }
}



