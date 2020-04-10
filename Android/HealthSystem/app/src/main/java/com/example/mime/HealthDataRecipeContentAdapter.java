package com.example.mime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
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
import com.example.entity.RecipeItemClient;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;

import java.util.ArrayList;
import java.util.List;

public class HealthDataRecipeContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<RecipeItemClient> contentItems;
    private boolean hasMore = true;
    private int CONTENT_TYPE = 0;
    private int MORE_TYPE = 1;
    private int kind;


    //创建构造函数
    public HealthDataRecipeContentAdapter(Context context, List<RecipeItemClient> recipeItemClients, int kind) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.contentItems = recipeItemClients;//实体类数据ArrayList
        this.kind = kind;
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
        if (kind != 2) {
            if (viewType == CONTENT_TYPE)
                return new ContentViewHolder(inflater.inflate(R.layout.recipe_item_add, parent, false));
            else
                return new MoreDataViewHolder(inflater.inflate(R.layout.more_data, parent, false));
        } else {
            return new ContentViewHolder(inflater.inflate(R.layout.recipe_item_add, parent, false));
        }
    }


    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            ContentViewHolder viewHolder = (ContentViewHolder) holder;
            final RecipeItemClient data = contentItems.get(position);
            viewHolder.recipeName.setText(data.getName());//获取实体类中的name字段并设置
            Bitmap bitmap = PicUtils.byteConvertToBitmap(data.getPic());
            RequestOptions options = new RequestOptions().error(R.drawable.image1).bitmapTransform(new RoundedCorners(30));//图片圆角为30
            Glide.with(context).load(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null))) //图片地址
                    .apply(options)
                    .into(viewHolder.recipeImage);

            viewHolder.recipeNum.setText(data.getNum());
            viewHolder.recipeAuthor.setText(data.getAuthor());
            viewHolder.addRecipeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("recipeNum", data.getNum());
                    bundle.putString("recipeName", data.getName());
                    bundle.putString("recipeAuthor", data.getAuthor());
                    bundle.putByteArray("recipeImage", data.getPic());
                    bundle.putString("recipeKind", data.getRecipeKind());
                    bundle.putInt("recipeViewCount", data.getViewCount());
                    bundle.putInt("recipeCollectCount", data.getCollectCount());
                    if (kind == 1) {
                        System.out.println("添加按钮被点击");
                        Intent intent = new Intent();
                        intent.setAction("AddRecipeContentAction");
                        intent.putExtras(bundle);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    } else if (kind == 2) {
                        Intent intent = new Intent();
                        intent.setAction("DeleteRecipeContentAction");
                        intent.putExtras(bundle);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("DeleteRecipeAction");
                        intent.putExtras(bundle);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }
            });
        } else {
            //显示加载更多按钮
            MoreDataViewHolder moreDataViewHolder = (MoreDataViewHolder) holder;
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
        if (kind != 2)
            return contentItems.size() + 1;
        else
            return contentItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (kind != 2) {
            if (0 <= position && position < contentItems.size()) {
                return CONTENT_TYPE;
            } else {
                return MORE_TYPE;
            }
        } else
            return CONTENT_TYPE;
    }



    public void updateList(List<RecipeItemClient> newData, boolean hasMore) {
        if (newData != null) {
            contentItems.addAll(newData);
        }
        this.hasMore = hasMore;
    }


    public void updateContent(List<RecipeItemClient> newData) {
        contentItems.clear();
        contentItems.addAll(newData);
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
        private TextView recipeAuthor;
        private ImageView addRecipeImage;

        public ContentViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_item_add_image);
            recipeNum = itemView.findViewById(R.id.recipe_item_add_recipe_num);
            recipeName = itemView.findViewById(R.id.recipe_item_add_recipe_name);
            recipeAuthor = itemView.findViewById(R.id.recipe_item_add_recipe_author);
            addRecipeImage = itemView.findViewById(R.id.recipe_item_add_button);
            if (kind == 1)
                addRecipeImage.setImageResource(R.drawable.circle_add);
            if (kind == 2)
                addRecipeImage.setImageResource(R.drawable.delete);
            if (kind == 3)
                addRecipeImage.setImageResource(R.drawable.delete);

            itemView.findViewById(R.id.recipe_item_add_detail_layout).setOnClickListener(new View.OnClickListener() {
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