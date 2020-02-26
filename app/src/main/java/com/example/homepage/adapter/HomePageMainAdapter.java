package com.example.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.common.RecipeDetailActivity;
import com.example.common.SpacesItemDecoration;
import com.example.entity.RecipeItemClient;
import com.example.entity.HomePageMenu;
import com.example.R;
import com.example.utils.PicUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;


public class HomePageMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 菜单的viewpager坐标
     */
    private static final int MENU_TYPE = 0;
    /**
     * 实际的食谱内容
     */
    private static final int CONTENT_TYPE = 1;
    private static final int MORE_TYPE = 2;
    private Context context;
    private final static int HOME_PAGE_MENU_SIZE = 8;
    private List<RecipeItemClient> contents;
    private List<HomePageMenu> menus;
    private boolean hasMore = true;
    private boolean fadeTips = false;


    public HomePageMainAdapter(Context context, List<RecipeItemClient> contents, List<HomePageMenu> menus) {
        this.context = context;
        this.contents = contents;
        this.menus = menus;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //initData();
        switch (viewType) {
            case MENU_TYPE:
                return new MenuViewHolder(inflater.inflate(R.layout.home_page_menu_viewpager, parent, false));
            case CONTENT_TYPE:
                //此处的资源应修改为展示食谱的recyclerview
                return new ContentViewHolder(inflater.inflate(R.layout.recipe_item, parent, false));
            case MORE_TYPE:
                return new MoreDataViewHolder(inflater.inflate(R.layout.more_data, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MenuViewHolder) {
            final MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
            ViewPager viewPager = menuViewHolder.viewPager;
            int pageCount = (int) Math.ceil(menus.size() * 1.0 / HOME_PAGE_MENU_SIZE);
            List<View> viewList = new ArrayList<>();
            LayoutInflater inflater = LayoutInflater.from(this.context);
            for (int index = 0; index < pageCount; index++) {
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.home_page_menu_recycler_view, viewPager, false);
                HomePageMenuRecyclerAdapter entranceAdapter = new HomePageMenuRecyclerAdapter(this.context, menus, index, HOME_PAGE_MENU_SIZE);
                recyclerView.setAdapter(entranceAdapter);
                recyclerView.addItemDecoration(new SpacesItemDecoration());
                recyclerView.setLayoutManager(new GridLayoutManager(this.context, 4, GridLayoutManager.VERTICAL, false));
                viewList.add(recyclerView);
            }
            HomeMenuViewAdapter adapter = new HomeMenuViewAdapter(viewList);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(3);

        } else if (holder instanceof ContentViewHolder) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            // System.out.println("position为：" + position + "内容大小为：" + contents.size());
            // contentViewHolder.recipeImage.setImageBitmap();
            Bitmap bitmap = PicUtils.byteConvertToBitmap(contents.get(position - 1).getPic());
            RequestOptions options = new RequestOptions().error(R.drawable.image1).bitmapTransform(new RoundedCorners(30));//图片圆角为30
            Glide.with(context).load(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null))) //图片地址
                    .apply(options)
                    .into(contentViewHolder.recipeImage);
            contentViewHolder.recipeNum.setText(contents.get(position - 1).getNum());
            contentViewHolder.recipeName.setText(contents.get(position - 1).getName());
            contentViewHolder.recipeKind.setText(contents.get(position - 1).getRecipeKind());
            contentViewHolder.recipeCollectCount.setText(String.valueOf(contents.get(position - 1).getCollectCount()));
            contentViewHolder.recipeViewCount.setText(String.valueOf(contents.get(position - 1).getViewCount()));

        } else {
            //显示加载更多按钮
            MoreDataViewHolder moreDataViewHolder = (MoreDataViewHolder) holder;
            moreDataViewHolder.textView.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                //不隐藏footview提示

                //if (contents.size() > 0) {
                moreDataViewHolder.textView.setText("正在加载更多...");
                // }
            } else {
                moreDataViewHolder.textView.setText("没有更多数据了...");
               /* if (contents.size() > 0) {
                    //如果查询数据发现并没有增加时就显示没有更多数据了
                    moreDataViewHolder.textView.setText("没有更多数据了...");
                    //然后通过延时加载模拟网络请求时间，在
                    moreDataViewHolder.textView.setVisibility(View.GONE);
                }*/
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == MENU_TYPE) {
            return MENU_TYPE;
        } else if (0 < position && position <= contents.size()) {
            return CONTENT_TYPE;
        } else {
            return MORE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size() + 2;
    }


    //更新列表
    public void updateList(List<RecipeItemClient> newData, boolean hasMore) {

        if (newData != null) {
            contents.addAll(newData);
        }
        this.hasMore = hasMore;

    }

    public boolean isFadeTips() {
        return fadeTips;
    }


    /**
     * 自定义方法，获取列表中数据源的最后一个位置，比getitemcount少1，因为不计上底部的view
     */
    public int getRealLastPosition() {
        return contents.size() + 1;
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {

        private ViewPager viewPager;

        public MenuViewHolder(View menuView) {
            super(menuView);
            viewPager = itemView.findViewById(R.id.home_page_menu_view_pager);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage;
        private TextView recipeNum;
        private TextView recipeName;
        private TextView recipeKind;
        private TextView recipeViewCount;
        private TextView recipeCollectCount;
        /* private TextView textView;*/

        public ContentViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeNum = itemView.findViewById(R.id.recipe_num);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeKind = itemView.findViewById(R.id.recipe_kind);
            recipeCollectCount = itemView.findViewById(R.id.recipe_collect_count);
            recipeViewCount = itemView.findViewById(R.id.recipe_view_count);
            // textView = itemView.findViewById(R.id.more_data_text);

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

    public class MoreDataViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MoreDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.more_data_text);
        }
    }

}

