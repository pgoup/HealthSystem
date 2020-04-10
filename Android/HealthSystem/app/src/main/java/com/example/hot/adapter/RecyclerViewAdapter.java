package com.example.hot.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.entity.RecipeItemClient;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.myAdapter> {

    private Context context;
    private List<RecipeItemClient> contentItems;


    public RecyclerViewAdapter(Context context, List<RecipeItemClient> contentItems) {
        this.context = context;
        this.contentItems = contentItems;
    }

    @NonNull
    @Override
    public myAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.title_item, null);
        return new myAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter holder, int position) {
        holder.recipeName.setText(contentItems.get(position).getName());
       // holder.recipeImage.setImageBitmap(contentItems.get(position).getImage());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return contentItems.size();
    }
    
    class myAdapter extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private ImageView recipeImage;

        public myAdapter(@NonNull View itemView) {
            super(itemView);
            this.recipeName = itemView.findViewById(R.id.content_name);
           // this.recipeImage = itemView.findViewById(R.id.content_image);
            ViewGroup.LayoutParams params = recipeImage.getLayoutParams();
            params.width = 300;
            params.height = 300;
            recipeImage.setLayoutParams(params);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra("recipeNum", recipeName.getText());
                    context.startActivity(intent);
                }
            });*/

        }
    }
}
