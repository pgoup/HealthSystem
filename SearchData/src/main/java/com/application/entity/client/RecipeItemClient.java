package com.application.entity.client;

import lombok.Data;

import java.io.Serializable;

/** @author PG */
@Data
public class RecipeItemClient implements Serializable {
  private String num;
  private String name;
  private byte[] pic;
  private String recipeKind;
  private int viewCount;
  private int collectCount;
  private String author;

  public RecipeItemClient() {}

  public RecipeItemClient(String num, String name, byte[] pic, String recipeKind, int viewCount, int collectCount, String author) {
    this.num = num;
    this.name = name;
    this.pic = pic;
    this.recipeKind = recipeKind;
    this.viewCount = viewCount;
    this.collectCount = collectCount;
    this.author = author;
  }
}
