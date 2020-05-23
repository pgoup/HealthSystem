package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Table(name = "user_collect_recipes")
@Data
public class UserCollectRecipes {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_account")
  private Long userAccount;

  @Column(name = "recipe_num")
  private long recipeNum;

  public UserCollectRecipes(Long userAccount, long recipeNum) {
    this.userAccount = userAccount;
    this.recipeNum = recipeNum;
  }

  public UserCollectRecipes() {}
}
