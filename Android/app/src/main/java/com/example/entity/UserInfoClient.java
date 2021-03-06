package com.example.entity;

import java.util.List;

public class UserInfoClient {
    private String account;
    private String userName;
    private String sex;
    private byte[] pic;
    private String intro;
    // 粉丝数量
    private int fans;
    // 关注数量
    private int attentions;

    private float height;
    private float weight;

    private List<UserAddNutrition> userAddNutrition;

    public UserInfoClient() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getAttentions() {
        return attentions;
    }

    public void setAttentions(int attentions) {
        this.attentions = attentions;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public List<UserAddNutrition> getUserAddNutrition() {
        return userAddNutrition;
    }

    public void setUserAddNutrition(List<UserAddNutrition> userAddNutrition) {
        this.userAddNutrition = userAddNutrition;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
