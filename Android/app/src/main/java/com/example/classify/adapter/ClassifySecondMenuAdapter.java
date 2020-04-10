package com.example.classify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.classify.HealthDietDetailActivity;
import com.example.R;

import java.util.List;

public class ClassifySecondMenuAdapter extends RecyclerView.Adapter<ClassifySecondMenuAdapter.myViewHodler> {
    private Context context;
    private List<String> secondMenus;


    //创建构造函数
    public ClassifySecondMenuAdapter(Context context, List<String> menus) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.secondMenus = menus;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */

    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = View.inflate(context, R.layout.title_item, null);
        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(myViewHodler holder, int position) {
        //根据点击位置绑定数据
        String data = secondMenus.get(position);
        holder.menuName.setText(data);//获取实体类中的name字段并设置
        //holder.menuImage.setImageBitmap(data.getImage());
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return secondMenus.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {
        ///private RoundImageView menuImage;
        private TextView menuName;

        public myViewHodler(View itemView) {
            super(itemView);
            //  menuImage = itemView.findViewById(R.id.content_image);
            menuName = itemView.findViewById(R.id.content_name);
            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HealthDietDetailActivity.class);
                    intent.putExtra("healthDietName", menuName.getText());
                    context.startActivity(intent);
                }
            });

        }
    }
}