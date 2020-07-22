package com.example.nutritionQuery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.nutritionQuery.FoodNutritionActivity;
import com.example.entity.ContentItem;
import com.example.R;

import java.util.List;

public class FoodsRecyclerAdapter extends RecyclerView.Adapter<FoodsRecyclerAdapter.myAdapter> {
    private Context context;
    private List<ContentItem> contentItems;

    public FoodsRecyclerAdapter(Context context, List<ContentItem> contentItems) {
        this.context = context;
        this.contentItems = contentItems;
    }

    @NonNull
    @Override
    public myAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myAdapter(View.inflate(context, R.layout.title_item, null));
    }


    @Override
    public void onBindViewHolder(@NonNull myAdapter holder, int position) {
        holder.recipeName.setText(contentItems.get(position).getName());
        Bitmap bitmap = contentItems.get(position).getImage();
        RequestOptions options = new RequestOptions().error(R.drawable.image1).bitmapTransform(new RoundedCorners(30));//图片圆角为30
        Glide.with(context).load(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null))) //图片地址
                .apply(options)
                .into(holder.recipeImage);

        holder.recipeImage.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return contentItems == null ? 0 : contentItems.size();
    }

    class myAdapter extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private ImageView recipeImage;

        public myAdapter(@NonNull final View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.content_name);
            recipeImage = itemView.findViewById(R.id.content_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FoodNutritionActivity.class);
                    intent.putExtra("foodName", recipeName.getText());
                    context.startActivity(intent);
                }
            });

        }
    }
}
