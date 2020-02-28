package com.application.entity.client;

import lombok.Data;

/** @author PG */
@Data
public class RecipeMeasureItem {
  private long  recipeNum;
  private int num;
  private String measure;
  private byte[] pic;

  public RecipeMeasureItem(long recipeNum, int num, String measure, byte[] pic) {
    this.recipeNum = recipeNum;
    this.num = num;
    this.measure = measure;
    this.pic = pic;
  }
}
