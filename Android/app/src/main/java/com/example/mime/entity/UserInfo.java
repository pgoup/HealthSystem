package com.example.mime.entity;

public class UserInfo {
    private Long account;
    private String userName;
    private byte[] pic;
    private String intro;
    //粉丝数量
    private int fans;
    //关注数量
    private int attentions;

    public UserInfo() {
    }

    public UserInfo(Long account, String userName, byte[] pic, String intro, int fans, int attentions) {
        this.account = account;
        this.userName = userName;
        this.pic = pic;
        this.intro = intro;
        this.fans = fans;
        this.attentions = attentions;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
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
}
