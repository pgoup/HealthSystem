package com.example.mime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.entity.RecipeItemClient;
import com.example.entity.UserInfoClient;
import com.example.mime.activity.MimeConcernedAndFansActivity;
import com.example.utils.PicUtils;

import java.util.List;

public class MimeConcernFanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<UserInfoClient> contentItems;
    private boolean hasMore = true;
    private int CONTENT_TYPE = 0;
    private int MORE_TYPE = 1;


    //创建构造函数
    public MimeConcernFanAdapter(Context context, List<UserInfoClient> userInfoClients) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.contentItems = userInfoClients;//实体类数据ArrayList
    }

    public void setContentItems(List<UserInfoClient> contentItems) {
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
            return new MimeConcernFanAdapter.ContentViewHolder(inflater.inflate(R.layout.author_item, parent, false));
        else
            return new MimeConcernFanAdapter.MoreDataViewHolder(inflater.inflate(R.layout.more_data, parent, false));

    }


    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MimeConcernFanAdapter.ContentViewHolder) {
            MimeConcernFanAdapter.ContentViewHolder viewHolder = (MimeConcernFanAdapter.ContentViewHolder) holder;
            UserInfoClient data = contentItems.get(position);
            viewHolder.userName.setText(data.getUserName());//获取实体类中的name字段并设置
            viewHolder.authorImage.setImageBitmap(PicUtils.byteConvertToBitmap(data.getPic()));
            viewHolder.userAccount.setText(data.getAccount());
            viewHolder.concernedCount.setText(String.valueOf(data.getAttentions()));
            viewHolder.fanCount.setText(String.valueOf(data.getFans()));
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

    public void updateList(List<UserInfoClient> newData, boolean hasMore) {
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
        private ImageView authorImage;
        private TextView userAccount;
        private TextView userName;
        private TextView concernedCount;
        private TextView fanCount;

        public ContentViewHolder(View itemView) {
            super(itemView);
            authorImage = itemView.findViewById(R.id.author_item_image);
            userAccount = itemView.findViewById(R.id.author_item_user_account);
            userName = itemView.findViewById(R.id.author_item_user_name);
            fanCount = itemView.findViewById(R.id.author_item_fan_count);
            concernedCount = itemView.findViewById(R.id.author_item_concerned_count);

            /**设置点击事件*/
          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra("userAccount", userAccount.getText());
                    context.startActivity(intent);
                    System.out.println("点击");
                }
            });*/

        }
    }
}

