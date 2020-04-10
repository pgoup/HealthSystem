package com.example.mime.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.mime.entity.UserInfo;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

public class MimeLoginActivity extends AppCompatActivity {
    private EditText userNum;
    private EditText password;
    private Button login;
    private Button register;
    public static final String ACTION = "android.mime.action.login";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_login);
        userNum = findViewById(R.id.mime_login_user_num);
        password = findViewById(R.id.mime_login_password);
        login = findViewById(R.id.mime_login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = userNum.getText().toString();
                String pass = password.getText().toString();
                if (!existUserInfo(account)) {
                    InternetTooltip.tooltip(MimeLoginActivity.this, "不存在该账号，清先注册");
                    return;
                }
                UserInfo userInfo = getUserInfo(account, pass);
                if (userInfo == null) {
                    InternetTooltip.tooltip(MimeLoginActivity.this, "账号或密码错误，请重新输入");
                    return;
                }
                System.out.println("登录并保存用户信息：" + account);
                UserManager.getInstance().saveUserInfo(getApplicationContext(), userInfo);
                System.out.println("登录后的用户账号为：" + UserManager.getInstance().hasUserInfo(getApplicationContext()));
                Intent intent = new Intent("android.mime.action.login");
                intent.putExtra("data", "refresh");
                LocalBroadcastManager.getInstance(MimeLoginActivity.this).sendBroadcast(intent);
                //sendBroadcast(intent);
                Handler handler = new Handler();
                Message message = new Message();
                message.what = 2;
                handler.sendMessageDelayed(message, 0);
                finish();
            }
        });
        register = findViewById(R.id.mime_register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = userNum.getText().toString();
                String pass = password.getText().toString();
                if (existUserInfo(account)) {
                    InternetTooltip.tooltip(MimeLoginActivity.this, "账号已存在，清直接登录");
                    return;
                }
                registerUserInfo(account, pass);
                finish();
            }
        });
    }

    /**
     * 验证是否存在指定账号
     */
    private boolean existUserInfo(String account) {
        String param = "account=" + account;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/existUserInfo", param);
        if (result == null) {
            InternetTooltip.tooltip(MimeLoginActivity.this, "请求出现异常，请检查是否网络未连接");
            return false;
        }
        return result.equals("true") ? true : false;

    }


    /**
     * 根据用户名和密码获取用户信息
     */
    private UserInfo getUserInfo(String account, String pass) {
        String params = "account=" + account + "&password=" + pass;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getUserInfo", params);
        if (result == null) {
            InternetTooltip.tooltip(MimeLoginActivity.this, "请求出现异常，请检查是否网络未连接");
            return null;
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(result), UserInfo.class);
    }

    /**
     * 注册用户名和密码
     */
    private void registerUserInfo(String account, String pass) {
        String params = "account=" + account + "&password=" + pass;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/saveUserInfo", params);
        if (result == null) {
            InternetTooltip.tooltip(MimeLoginActivity.this, "请求出现异常，请检查是否网络未连接");
            return;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAttentions(0);
        userInfo.setFans(0);
        userInfo.setIntro("暂无简介");
        userInfo.setUserName(account);
        UserManager.getInstance().saveUserInfo(MimeLoginActivity.this, userInfo);
        return;
    }
}
