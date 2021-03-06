package com.application.entity.client;

import com.application.entity.server.UserAddNutrition;
import lombok.Data;

import java.util.List;

/** @author PG */
@Data
public class UserInfoClient {
  private Long account;
  private String userName;
  private byte[] pic;
  private String intro;
  // 粉丝数量
  private int fans;
  // 关注数量
  private int attentions;

  private float height;
  private float weight;
  private List<UserAddNutrition> userAddNutrition;
  private String sex;

  public UserInfoClient() {}

  public UserInfoClient(
      Long account,
      String userName,
      byte[] pic,
      String intro,
      int fans,
      int attentions,
      float height,
      float weight,
      String sex) {
    this.account = account;
    this.userName = userName;
    this.pic = pic;
    this.intro = intro;
    this.fans = fans;
    this.attentions = attentions;
    this.height = height;
    this.weight = weight;
    this.sex = sex;
  }
}
