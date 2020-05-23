package com.example.classify.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.common.RecipeDetailActivity;
import com.example.entity.HealthDietClient;
import com.example.entity.RecipeItemClient;
import com.example.utils.PicUtils;

import java.util.List;

public class HealthDietDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEALTH_DIET_DETAIL_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private static final int MORE_TYPE = 2;
    private Context context;
    private List<RecipeItemClient> contents;
    private HealthDietClient healthDietClient;
    private boolean hasMore = true;

    public HealthDietDetailAdapter(Context context, List<RecipeItemClient> contents, HealthDietClient healthDietClient) {
        this.context = context;
        this.contents = contents;
        this.healthDietClient = healthDietClient;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HEALTH_DIET_DETAIL_TYPE:
                return new HealthDietDetailViewHolder(inflater.inflate(R.layout.health_diet_detail, parent, false));
            case CONTENT_TYPE:
                return new HealthDietDetailAdapter.ContentViewHolder(inflater.inflate(R.layout.recipe_item, parent, false));
            case MORE_TYPE:
                return new HealthDietDetailAdapter.MoreDataViewHolder(inflater.inflate(R.layout.more_data, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HealthDietDetailViewHolder) {
            HealthDietDetailViewHolder healthDietDetailViewHolder = (HealthDietDetailViewHolder) holder;
            healthDietDetailViewHolder.healthDietMethod.setText(healthDietClient.getDietMethod());
            healthDietDetailViewHolder.suitable.setText(healthDietClient.getSuitable());
            healthDietDetailViewHolder.suitableFoods.setText(healthDietClient.getSuitableFoods());
            healthDietDetailViewHolder.taboo.setText(healthDietClient.getTaboo());
            healthDietDetailViewHolder.tabooFoods.setText(healthDietClient.getTabooFoods());
        } else if (holder instanceof HealthDietDetailAdapter.ContentViewHolder) {
            HealthDietDetailAdapter.ContentViewHolder contentViewHolder = (HealthDietDetailAdapter.ContentViewHolder) holder;
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
            HealthDietDetailAdapter.MoreDataViewHolder moreDataViewHolder = (HealthDietDetailAdapter.MoreDataViewHolder) holder;
            moreDataViewHolder.textView.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                moreDataViewHolder.textView.setText("正在加载更多...");
            } else {
                moreDataViewHolder.textView.setText("没有更多数据了...");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEALTH_DIET_DETAIL_TYPE) {
            return HEALTH_DIET_DETAIL_TYPE;
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

    public class HealthDietDetailViewHolder extends RecyclerView.ViewHolder {

        private TextView healthDietMethod;
        private TextView suitable;
        private TextView suitableFoods;
        private TextView taboo;
        private TextView tabooFoods;


        public HealthDietDetailViewHolder(View itemView) {
            super(itemView);
            healthDietMethod = itemView.findViewById(R.id.health_diet_method);
            suitable = itemView.findViewById(R.id.health_diet_suitable);
            suitableFoods = itemView.findViewById(R.id.health_diet_suitable_foods);
            taboo = itemView.findViewById(R.id.health_diet_taboo);
            tabooFoods = itemView.findViewById(R.id.health_diet_taboo_foods);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage;
        private TextView recipeNum;
        private TextView recipeName;
        private TextView recipeKind;
        private TextView recipeViewCount;
        private TextView recipeCollectCount;


        public ContentViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeNum = itemView.findViewById(R.id.recipe_num);
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

    public class MoreDataViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MoreDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.more_data_text);
        }
    }
}