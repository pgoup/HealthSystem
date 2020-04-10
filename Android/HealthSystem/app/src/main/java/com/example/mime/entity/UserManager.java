package com.example.mime.entity;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static UserManager instance;

    public UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, UserInfo userInfo) {
        SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("account", userInfo.getAccount().toString());
        editor.putString("userName", userInfo.getUserName());
        editor.commit();
    }

    /**
     * 获取用户的信息
     */
    public UserInfo getUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (preferences == null)
            return null;
        String account = preferences.getString("account", "");
        if (account.isEmpty())
            return null;
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(preferences.getString("userName", ""));
       /* userInfo.setIntro(preferences.getString("intro", ""));
        userInfo.setAttentions(preferences.getInt("attentions", 0));
        userInfo.setFans(preferences.getInt("fans", 0));
      */
        userInfo.setAccount(Long.parseLong(preferences.getString("account", "")));
        return userInfo;
    }

    /**
     * 查看缓存中是否存在用户信息
     */
    public boolean hasUserInfo(Context context) {
        UserInfo userInfo = getUserInfo(context);
        if (userInfo != null) {
            return true;
        }
        return false;
    }

    /**
     * 清楚缓存
     */
    public void logOut(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
