package com.example.mime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.mime.activity.MimeSelectTagsActivity;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private Context context;
    private List<String> values = new ArrayList<>();
    private static final int TAG_TYPE = 1;
    private static final int ADD_TAG_TYPE = 2;

    public TagAdapter(Context context, List<String> values) {
        this.context = context;
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.tag_item, null);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.button.setText(values.get(position));
        final int pos = position;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(pos == values.size() - 1, values.get(pos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void removeItem(int pos) {
        this.values.remove(pos);
        notifyDataSetChanged();
        if (pos != values.size()) {
            notifyItemChanged(pos, values.size() - pos);
        }
    }

    private void dialog(boolean lastOne, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.mime_dialog_view, null);
        LinearLayout linearLayout = view.findViewById(R.id.mime_dialog_layout);
        Button firstButton = new Button(context);
        Button cancelButton = new Button(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String text = lastOne ? "添加" : "删除";
        firstButton.setText(text);
        cancelButton.setText("取消");
        firstButton.setLayoutParams(layoutParams);
        cancelButton.setLayoutParams(layoutParams);
        linearLayout.addView(firstButton);
        linearLayout.addView(cancelButton);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.setView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final String value = message;
        final boolean last = lastOne;
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!last) {
                    Long userAccount = UserManager.getInstance().getUserInfo(context).getAccount();
                    String param = "userAccount=" + userAccount + "&tag=" + value;
                    String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "deleteUserTags", param);
                    //System.out.println("返回的结果为:" + result);
                    if (result == null) {
                        InternetTooltip.tooltip(context, HttpClientUtils.HTTP_REQUEST_ERROR);
                    } else {
                        InternetTooltip.tooltip(context, "删除成功！");
                        values.remove(value);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                } else {
                    Intent intent = new Intent(context, MimeSelectTagsActivity.class);
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < values.size() - 1; i++) {
                        builder.append(values.get(i)).append("&&");
                    }
                    intent.putExtra("value", builder.toString());
                    dialog.dismiss();
                    context.startActivity(intent);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.mime_tag_text);

        }
    }
}
