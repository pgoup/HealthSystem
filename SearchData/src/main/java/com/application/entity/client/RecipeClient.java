package com.application.entity.client;

import lombok.Data;

import java.util.List;

/** @author PG */
@Data
public class RecipeClient {

  private long number;

  private String kind;

  private String recipeName;
  /** 图片的字节流形式 */
  private byte[] pic;

  /** 主食材 */
  private String MainIngredient;

  /** 辅食材 */
  private String accessories;

  /** 制作方法 */
  private List<RecipeMeasureItem> measure;

  private int viewCount;
  private int collectNum;
  private String author;
  // 是否收藏
  private boolean isCollected;
  // 是否关注
  private boolean isConcerned;
  private byte[] authorImage;
  private Long authorAccount;

  public RecipeClient() {}

  public RecipeClient(long number, String kind, String recipeName, byte[] pic, String mainIngredient, String accessories, List<RecipeMeasureItem> measure, int viewCount, int collectNum, String author, boolean isCollected, boolean isConcerned, byte[] authorImage, Long authorAccount) {
    this.number = number;
    this.kind = kind;
    this.recipeName = recipeName;
    this.pic = pic;
    MainIngredient = mainIngredient;
    this.accessories = accessories;
    this.measure = measure;
    this.viewCount = viewCount;
    this.collectNum = collectNum;
    this.author = author;
    this.isCollected = isCollected;
    this.isConcerned = isConcerned;
    this.authorImage = authorImage;
    this.authorAccount = authorAccount;
  }
}
