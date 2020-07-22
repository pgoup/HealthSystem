package com.example.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.classify.ClassifyDetailActivity;
import com.example.entity.RecipePicAndNameClient;
import com.example.utils.PicUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AllCommonRecipeRecyclerAdapter extends RecyclerView.Adapter<AllCommonRecipeRecyclerAdapter.RecyclerViewHolder> {

    private Context context;
    private List<RecipePicAndNameClient> recipeClients;

    public AllCommonRecipeRecyclerAdapter(Context context, List<RecipePicAndNameClient> recipeClients) {
        this.context = context;
        this.recipeClients = recipeClients;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.home_page_menu_item, null);
        return new AllCommonRecipeRecyclerAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textView.setText(recipeClients.get(position).getName());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(new BitmapDrawable(PicUtils.byteConvertToBitmap(recipeClients.get(position).getPic())))
                .build();
        RoundingParams params = RoundingParams.fromCornersRadius(5f);
        params.setRoundAsCircle(true);
        hierarchy.setRoundingParams(params);
        holder.imageView.setHierarchy(hierarchy);
    }

    @Override
    public int getItemCount() {
        return recipeClients.size();
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private SimpleDraweeView imageView;

        public RecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.home_page_menu_text);
            imageView = itemView.findViewById(R.id.home_page_menu_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClassifyDetailActivity.class);
                    intent.putExtra("kindName", textView.getText());
                    context.startActivity(intent);
                }
            });
        }
    }
}
