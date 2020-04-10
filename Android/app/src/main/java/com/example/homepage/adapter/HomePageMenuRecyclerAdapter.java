package com.example.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classify.ClassifyDetailActivity;
import com.example.entity.HomePageMenu;
import com.example.R;
import com.example.utils.PicUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class HomePageMenuRecyclerAdapter extends RecyclerView.Adapter<HomePageMenuRecyclerAdapter.RecyclerViewHolder> {

    private List<HomePageMenu> menus;
    /**
     * 页数下标，从0开始，第几页
     */
    private int index;
    /**
     * 每页显示最大条目个数
     */
    private int maxPageSize;
    private Context context;
    // private final LayoutInflater layoutInflater;

    public HomePageMenuRecyclerAdapter(Context context, List<HomePageMenu> menus, int index, int maxPageSize) {
        this.menus = menus;
        this.index = index;
        this.maxPageSize = maxPageSize;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.home_page_menu_item, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final int pos = index * maxPageSize + position;
        holder.textView.setText(menus.get(pos).getMenuName());
        //holder.imageView.setImageResource(menus.get(pos).getMenuImage());
        //Bitmap bitmap = PicUtils.byteConvertToBitmap(userInfo.getPic());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(menus.get(pos).getMenuImage())
                .build();
        RoundingParams params = RoundingParams.fromCornersRadius(5f);
        params.setRoundAsCircle(true);
        hierarchy.setRoundingParams(params);
        holder.imageView.setHierarchy(hierarchy);

    }

    @Override
    public int getItemCount() {
        return menus.size() > (index + 1) * maxPageSize ? maxPageSize : (menus.size() - index * maxPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + index * maxPageSize;
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
