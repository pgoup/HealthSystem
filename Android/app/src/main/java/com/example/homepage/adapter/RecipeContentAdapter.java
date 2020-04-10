package com.example.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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
import com.example.common.RecipeDetailActivity;
import com.example.R;
import com.example.entity.RecipeItemClient;
import com.example.utils.PicUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class RecipeContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<RecipeItemClient> contentItems;
    private boolean hasMore = true;
    private int CONTENT_TYPE = 0;
    private int MORE_TYPE = 1;


    //创建构造函数
    public RecipeContentAdapter(Context context, List<RecipeItemClient> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.contentItems = goodsEntityList;//实体类数据ArrayList
    }

    public void setContentItems(List<RecipeItemClient> contentItems) {
        this.contentItems = contentItems;
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //initData();
        if (viewType == CONTENT_TYPE)
            return new ContentViewHolder(inflater.inflate(R.layout.recipe_item, parent, false));
        else
            return new MoreDataViewHolder(inflater.inflate(R.layout.more_data, parent, false));

    }


    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeContentAdapter.ContentViewHolder) {
            RecipeContentAdapter.ContentViewHolder viewHolder = (RecipeContentAdapter.ContentViewHolder) holder;
            RecipeItemClient data = contentItems.get(position);
            viewHolder.recipeName.setText(data.getName());//获取实体类中的name字段并设置
            System.out.println("图片的数组为：" + data.getPic());
            Bitmap bitmap = PicUtils.byteConvertToBitmap(data.getPic());
            RequestOptions options = new RequestOptions().error(R.drawable.image1).bitmapTransform(new RoundedCorners(30));//图片圆角为30
            Glide.with(context).load(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null))) //图片地址
                    .apply(options)
                    .into(viewHolder.recipeImage);

            viewHolder.recipeNum.setText(data.getNum());
            viewHolder.recipeKind.setText(data.getRecipeKind());
            viewHolder.recipeViewCount.setText(String.valueOf(data.getViewCount()));
            viewHolder.recipeCollectCount.setText(String.valueOf(data.getCollectCount()));
        } else {
            //显示加载更多按钮
            RecipeContentAdapter.MoreDataViewHolder moreDataViewHolder = (RecipeContentAdapter.MoreDataViewHolder) holder;
            moreDataViewHolder.textView.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                //不隐藏footview提示
                if (contentItems.size() > 0)
                    moreDataViewHolder.textView.setText("正在加载更多...");
                else
                    moreDataViewHolder.textView.setText("当前没有数据...");
            } else {
                moreDataViewHolder.textView.setText("没有更多数据了...");
            }

        }
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return contentItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 <= position && position < contentItems.size()) {
            return CONTENT_TYPE;
        } else {
            return MORE_TYPE;
        }
    }

    public void updateList(List<RecipeItemClient> newData, boolean hasMore) {
        if (newData != null) {
            contentItems.addAll(newData);
        }
        this.hasMore = hasMore;
    }

    public void resetData() {
        contentItems.clear();
    }

    public class MoreDataViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MoreDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.more_data_text);
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
                    System.out.println("点击");
                }
            });

        }
    }
}