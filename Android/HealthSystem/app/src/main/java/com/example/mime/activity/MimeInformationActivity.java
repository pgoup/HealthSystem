package com.example.mime.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.entity.UserInfoClient;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

public class MimeInformationActivity extends AppCompatActivity {
    private UserInfoClient userInfoClient = new UserInfoClient();
    private TextView userName;
    private TextView userAccount;
    private TextView userHeight;
    private TextView userWeight;
    private AlertDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_information);
        postRequest();
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
    }

    private void postRequest() {
        String param = "userAccount=" + UserManager.getInstance().getUserInfo(MimeInformationActivity.this).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getUserDetailInfo", param);
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        JSONObject object = JSONObject.parseObject(result);
        userInfoClient = JSONObject.toJavaObject(object, UserInfoClient.class);
    }

    private void changeIntro(String content, final String updateKind) {
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

        String param = "data=" + info + "&userAccount=" + userInfoClient.getAccount() + "&updateKind=" + updateKind;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "uploadUserInfo", param);
        if (result == null || result.equals("false")) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("提交成功！");
            builder.show();
            if (updateKind.equals("userName"))
                userName.setText(info);
            else if (updateKind.equals("userHeight"))
                userHeight.setText(Float.valueOf(info).toString());
            else userWeight.setText(Float.valueOf(info).toString());
        }
        dialog.dismiss();
    }


}
