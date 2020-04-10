package com.example.classify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.common.RecipeDetailActivity;
import com.example.entity.RecipeItemClient;
import com.example.utils.PicUtils;

import java.util.List;

public class ClassifyDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int MENU_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private static final int MORE_TYPE = 2;
    private Context context;
    private List<RecipeItemClient> contents;
    private boolean hasMore = true;

    public ClassifyDetailAdapter(Context context, List<RecipeItemClient> contents) {
        this.context = context;
        this.contents = contents;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CONTENT_TYPE:
                //此处的资源应修改为展示食谱的recyclerview
                return new ClassifyDetailAdapter.ContentViewHolder(inflater.inflate(R.layout.recipe_item, parent, false));
            case MORE_TYPE:
                return new ClassifyDetailAdapter.MoreDataViewHolder(inflater.inflate(R.layout.more_data, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ClassifyDetailAdapter.ContentViewHolder) {
            ClassifyDetailAdapter.ContentViewHolder contentViewHolder = (ClassifyDetailAdapter.ContentViewHolder) holder;
            Bitmap bitmap = PicUtils.byteConvertToBitmap(contents.get(position).getPic());
            RequestOptions options = new RequestOptions().error(R.drawable.image1).bitmapTransform(new RoundedCorners(30));//图片圆角为30
            Glide.with(context).load(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null))) //图片地址
                    .apply(options)
                    .into(contentViewHolder.recipeImage);

            contentViewHolder.recipeName.setText(contents.get(position).getName());
            contentViewHolder.recipeNum.setText(contents.get(position).getNum());
            contentViewHolder.recipeKind.setText(contents.get(position).getRecipeKind());
            contentViewHolder.recipeCollectCount.setText(String.valueOf(contents.get(position).getCollectCount()));
            contentViewHolder.recipeViewCount.setText(String.valueOf(contents.get(position).getViewCount()));

        } else {
            //显示加载更多按钮
            ClassifyDetailAdapter.MoreDataViewHolder moreDataViewHolder = (ClassifyDetailAdapter.MoreDataViewHolder) holder;
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
        if (0 <= position && position <= contents.size()) {
            return CONTENT_TYPE;
        } else {
            return MORE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size() + 1;
    }

    //更新列表
    public void updateList(List<RecipeItemClient> newData, boolean hasMore) {
        if (newData != null) {
            contents.addAll(newData);
        }
        this.hasMore = hasMore;
    }


    /**
     * 自定义方法，获取列表中数据源的最后一个位置，比getitemcount少1，因为不计上底部的view
     */
    public int getRealLastPosition() {
        return contents.size() + 1;
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;

        public MenuViewHolder(View menuView) {
            super(menuView);
            linearLayout = itemView.findViewById(R.id.classify_detail_menu_item);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage;
        private TextView recipeName;
        private TextView recipeKind;
        private TextView recipeViewCount;
        private TextView recipeCollectCount;
        private TextView recipeNum;


        public ContentViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeNum = itemView.findViewById(R.id.recipe_num);
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
