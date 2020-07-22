package com.example.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.RecipeDetailActivity;
import com.example.entity.RecipeItemClient;
import com.example.entity.HomePageMenu;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePageRootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ViewPager viewPager;
    private List<RecipeItemClient> clients = new ArrayList<>();
    private List<HomePageMenu> menus = new ArrayList<>();
    private final static int HOME_PAGE_MENU_SIZE = 8;
    private Context context;
    private static final int MENU_TYPE = 0;

    public HomePageRootAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        initData();
        if (viewType == MENU_TYPE) {
            return new HomePageRootAdapter.MenuViewHolder(inflater.inflate(R.layout.home_page_menu_viewpager, parent, false));
        } else {
            return new HomePageRootAdapter.ContentViewHolder(inflater.inflate(R.layout.recipe_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MenuViewHolder) {
            final HomePageRootAdapter.MenuViewHolder menuViewHolder = (HomePageRootAdapter.MenuViewHolder) holder;
            ViewPager viewPager = menuViewHolder.viewPager;
            int pageCount = (int) Math.ceil(menus.size() * 1.0 / HOME_PAGE_MENU_SIZE);
            List<View> viewList = new ArrayList<>();
            LayoutInflater inflater = LayoutInflater.from(this.context);
            for (int index = 0; index < pageCount; index++) {
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.home_page_menu_recycler_view, viewPager, false);
                HomePageMenuRecyclerAdapter entranceAdapter = new HomePageMenuRecyclerAdapter(this.context, menus, index, HOME_PAGE_MENU_SIZE);
                recyclerView.setAdapter(entranceAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(this.context, 4, GridLayoutManager.VERTICAL, false));
                viewList.add(recyclerView);
            }
            HomeMenuViewAdapter adapter = new HomeMenuViewAdapter(viewList);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(3);
        } else {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            RecipeItemClient data = clients.get(position + 1);
            contentViewHolder.recipeName.setText(data.getName());//获取实体类中的name字段并设置
            contentViewHolder.recipeImage.setImageBitmap(PicUtils.byteConvertToBitmap(data.getPic()));
            contentViewHolder.recipeKind.setText("食谱类别 " + data.getRecipeKind());
            contentViewHolder.recipeViewCount.setText("食谱人气 " + data.getViewCount());
            contentViewHolder.recipeCollectCount.setText("收藏 " + data.getCollectCount());
        }
    }

    @Override
    public int getItemCount() {
        return clients.size() + 1;
    }

    private void initData() {
        initMenu();
        initContent();
    }

    private void initMenu() {
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
    }

    private void initContent() {
        String param = "kindName=家常菜";
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getRecipesByRecipeKind", param);
        if (result == null) {
            InternetTooltip.tip(context,HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        clients = JSONObject.parseArray(result, RecipeItemClient.class);

    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        private ViewPager viewPager;

        public MenuViewHolder(View menuView) {
            super(menuView);
            viewPager = itemView.findViewById(R.id.home_page_menu_view_pager);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage;
        private TextView recipeNum;
        private TextView recipeName;
        private TextView recipeKind;
        private TextView recipeViewCount;
        private TextView recipeCollectCount;

        public ContentViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeNum=itemView.findViewById(R.id.recipe_num);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeKind = itemView.findViewById(R.id.recipe_kind);
            recipeCollectCount = itemView.findViewById(R.id.recipe_collect_count);
            recipeViewCount = itemView.findViewById(R.id.recipe_view_count);

            /**设置点击事件*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra("recipeNum", recipeNum.getText());
                    context.startActivity(intent);
                }
            });

        }
    }
}