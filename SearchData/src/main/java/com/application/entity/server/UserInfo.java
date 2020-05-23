package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Table(name = "user_info")
@Data
public class UserInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  @Column(unique = true)
  private Long userAccount;
  private String userName;
  private String password;
  private String pic;
  private String intro;
  private String sex;
  private int manKind;
  // 粉丝数量
  private int fans;
  // 关注数量
  private int attentions;

  private String tags;

  private float height;
  private float weight;

  public UserInfo() {}
}
