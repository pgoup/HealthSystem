package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Table(name = "user_recipes")
@Data
public class UserRecipes {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long recipeNum;
  private long userAccount;

  public UserRecipes() {}

  public UserRecipes(Long recipeNum, long userAccount) {
    this.recipeNum = recipeNum;
    this.userAccount = userAccount;
  }
}
