package com.example.mime.activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.entity.UserAddNutrition;
import com.example.entity.UserInfoClient;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import java.util.ArrayList;
import java.util.List;
public class MimeInformationActivity extends AppCompatActivity {
    private UserInfoClient userInfoClient = new UserInfoClient();
    private TextView userName;
    private TextView userAccount;
    private TextView userHeight;
    private TextView userWeight;
    private TextView userSex;
    private static ScrollView scrollView;
    private AlertDialog dialog;
    private Context context;
    private int width;
    private List<UserAddNutrition> nutritions = new ArrayList<>();
    private LocalBroadcastManager broadcastManager;
    private BroadCastReceiver broadCastReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_information);
        context = MimeInformationActivity.this;
        width = getResources().getDisplayMetrics().widthPixels / 2;
        postRequest();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadCastReceiver = new BroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("AddNutritionAction");
        broadcastManager.registerReceiver(broadCastReceiver, intentFilter);
        findViewById(R.id.user_info_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userName = findViewById(R.id.mime_info_user_name);
        userName.setText(userInfoClient.getUserName());
        findViewById(R.id.user_info_name_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIntro(userName.getText().toString(), "userName");
            }
        });
        userAccount = findViewById(R.id.mime_info_user_account);
        userAccount.setText(userInfoClient.getAccount());
        userSex = findViewById(R.id.mime_info_user_sex);
        userSex.setText(userInfoClient.getSex());
        findViewById(R.id.user_info_sex_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIntro(userSex.getText().toString(), "sex");
            }
        });
        userHeight = findViewById(R.id.mime_info_user_height);
        userHeight.setText(String.valueOf(userInfoClient.getHeight()));
        findViewById(R.id.user_info_height_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIntro(userHeight.getText().toString(), "userHeight");
            }
        });
        userWeight = findViewById(R.id.mime_info_user_weight);
        userWeight.setText(String.valueOf(userInfoClient.getWeight()));
        findViewById(R.id.user_info_weight_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIntro(userWeight.getText().toString(), "userWeight");
            }
        });
        findViewById(R.id.user_add_nutrition_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MimeInformationActivity.this, UserAddNutritionActivity.class);
                startActivity(intent);
            }
        });
        scrollView = findViewById(R.id.mime_user_add_nutrition_scrollview);
        initAddNutritions();
    }
    private void initAddNutritions() {
        scrollView.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        TextView textView1 = new TextView(context);
        textView1.setText("自定义的营养参数如下：");
        textView1.setGravity(Gravity.LEFT);
        textView1.setLayoutParams(layoutParams);
        linearLayout.addView(textView1);
        View view4 = new View(context);
        view4.setLayoutParams(layoutParams);
        linearLayout.addView(view4);
        if (nutritions.size() == 0) {
            TextView textView = new TextView(context);
            textView.setText("暂无自定义营养数据。。。");
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            scrollView.addView(textView);
            return;
        }
        for (final UserAddNutrition nutrition : nutritions) {
            LinearLayout linearLayout1 = new LinearLayout(context);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setLayoutParams(layoutParams);
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout2.setLayoutParams(layoutParams);
            LinearLayout linearLayout3 = new LinearLayout(context);
            linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout3.setLayoutParams(layoutParams);
            LinearLayout linearLayout4 = new LinearLayout(context);
            linearLayout4.setOrientation(LinearLayout.VERTICAL);
            linearLayout4.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(0, 0, 20, 0);
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams3.weight = 5;
            TextView textView = new TextView(context);
            textView.setText(nutrition.getNutritionName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setLayoutParams(layoutParams3);
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.delete);
            layoutParams2.width = 100;
            layoutParams2.height = 100;
            layoutParams2.gravity = Gravity.RIGHT;
            imageView.setLayoutParams(layoutParams2);
            linearLayout4.addView(imageView);
            LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams4.weight = 1;
            final String nutritionName = nutrition.getNutritionName();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param = "userAccount=" + UserManager.getInstance().getUserInfo(context).getAccount() + "&nutritionName=" + nutritionName;
                    String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "deleteUserAddNutrition", param);
                    if (result == null) {
                        InternetTooltip.tip(context, HttpClientUtils.HTTP_REQUEST_ERROR);
                    } else if (result.equals("false")) {
                        InternetTooltip.tip(context, "删除失败！");
                    } else {
                        InternetTooltip.tip(context, "删除成功！");
                        nutritions.remove(nutrition);
                        initAddNutritions();
                    }
                }
            });
            linearLayout4.setLayoutParams(layoutParams4);
            linearLayout3.addView(textView);
            linearLayout3.addView(linearLayout4);
            TextView maxText = new TextView(context);
            maxText.setText("最大值");
            maxText.setWidth(width);
            maxText.setGravity(Gravity.CENTER);
            maxText.setTextSize(15);
            TextView max = new TextView(context);
            max.setText(String.valueOf(nutrition.getMaxCount()));
            max.setWidth(width);
            max.setGravity(Gravity.CENTER);
            max.setTextSize(15);
            linearLayout1.addView(maxText);
            linearLayout1.addView(max);
            TextView minText = new TextView(context);
            minText.setText("最小值");
            minText.setWidth(width);
            minText.setTextSize(15);
            minText.setGravity(Gravity.CENTER);
            TextView min = new TextView(context);
            min.setText(String.valueOf(nutrition.getMinCount()));
            min.setWidth(width);
            min.setTextSize(15);
            min.setGravity(Gravity.CENTER);
            linearLayout2.addView(minText);
            linearLayout2.addView(min);
            View view = new View(context);
            LinearLayout.LayoutParams layoutParam2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
            view.setLayoutParams(layoutParam2);
            View view2 = new View(context);
            view2.setLayoutParams(layoutParam2);
            View view3 = new View(context);
            LinearLayout.LayoutParams layoutParam3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20);
            view3.setLayoutParams(layoutParam3);
            linearLayout.addView(linearLayout3);
            linearLayout.addView(view);
            linearLayout.addView(linearLayout1);
            linearLayout.addView(view2);
            linearLayout.addView(linearLayout2);
            linearLayout.addView(view3);
        }
        TextView textView = new TextView(context);
        textView.setText("单位提示：热量单位为大卡，脂肪、蛋白质、碳水化合物等为克，胆固醇、维生素B-E、钾、钙、钠、镁、磷、铁、锌、铜、锰、碘等元素单位为毫克、维生素A单位为微克。\n");
        textView.setGravity(Gravity.CENTER);
        linearLayout.addView(textView);
        scrollView.addView(linearLayout);
    }
    private void postRequest() {
        String param = "userAccount=" + UserManager.getInstance().getUserInfo(MimeInformationActivity.this).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getUserDetailInfo", param);
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        JSONObject object = JSONObject.parseObject(result);
        userInfoClient = JSONObject.toJavaObject(object, UserInfoClient.class);
        nutritions = userInfoClient.getUserAddNutrition();
    }
    private void changeIntro(final String content, final String updateKind) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.mime_dialog_view, null);
        LinearLayout linearLayout = view.findViewById(R.id.mime_dialog_layout);
        final EditText introText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        introText.setMinHeight(300);
        introText.setBackground(null);
        introText.setHint(content);
        introText.setLayoutParams(layoutParams);
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        Button submitButton = new Button(this);
        Button cancelButton = new Button(this);
        submitButton.setText("提交");
        submitButton.setWidth(getResources().getDisplayMetrics().widthPixels / 2);
        cancelButton.setText("取消");
        cancelButton.setWidth(getResources().getDisplayMetrics().widthPixels / 2);
        buttonLayout.addView(submitButton);
        buttonLayout.addView(cancelButton);
        linearLayout.addView(introText);
        linearLayout.addView(buttonLayout);
        dialog = dialogBuilder.create();
        dialog.setView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intro = introText.getText().toString();
                if (content.equals(intro)) {
                    InternetTooltip.tip(MimeInformationActivity.this, "内容相同，不需修改！");
                } else
                    uploadInfo(intro, updateKind);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void uploadInfo(String info, String updateKind) {
        if (updateKind.equals("sex")) {
            if (!info.equals("男") && !info.equals("女")) {
                InternetTooltip.tip(MimeInformationActivity.this, "请输入正确的性别！");
                return;
            }
        }
        String param = "data=" + info + "&userAccount=" + userInfoClient.getAccount() + "&updateKind=" + updateKind;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "uploadUserInfo", param);
        if (result == null || result.equals("false")) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        } else {

            if (updateKind.equals("userName")) {
                userName.setText(info);
                Bundle bundle = new Bundle();
                bundle.putString("userName", info);
                Intent intent = new Intent();
                intent.setAction("UpdateInformation");
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(MimeInformationActivity.this).sendBroadcast(intent);
            } else if (updateKind.equals("userHeight"))
                userHeight.setText(Float.valueOf(info).toString());
            else if (updateKind.equals("sex"))
                userSex.setText(info);
            else userWeight.setText(Float.valueOf(info).toString());
        }
        dialog.dismiss();
        InternetTooltip.tip(this, "提交成功！");
    }
    private class BroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("AddNutritionAction")) {
                UserAddNutrition userAddNutrition = new UserAddNutrition();
                userAddNutrition.setNutritionName(intent.getExtras().get("name").toString());
                userAddNutrition.setMinCount(Float.valueOf(intent.getExtras().get("min").toString()));
                userAddNutrition.setMaxCount(Float.valueOf(intent.getExtras().get("max").toString()));
                nutritions.add(userAddNutrition);
                initAddNutritions();
            }
        }
    }
}
