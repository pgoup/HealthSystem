package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/** @author PG 一到具体的菜谱 */
@Entity
@Data
public class Recipe {
  @Id
  @Column(name = "recipe_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, name = "recipe_num")
  private Long recipeNum;

  private String kind;

  @Column(name = "recipe_name")
  private String recipeName;
  /** 图片的路径 */
  @Column(name = "pic_path")
  private String picPath;

  /** 主食材 */
  @Column(columnDefinition = "LONGTEXT")
  private String MainIngredient;

  /** 辅食材 */
  @Column(columnDefinition = "LONGTEXT")
  private String accessories;

  /** 制作方法 */
  @Column(columnDefinition = "LONGTEXT")
  private String measure;

  @Column(name = "view_count")
  private Integer viewCount;

  @Column(name = "collect_count")
  private Integer collectCount;

  private String author;
  private Long authorAccount;

  // 关键食材
  @Column(name = "key_ingredient")
  private String keyIngredient;

  public Recipe() {}

  public Recipe(
      Long recipeNum,
      String kind,
      String recipeName,
      String picPath,
      String mainIngredient,
      String accessories,
      String measure,
      Integer viewCount,
      Integer collectCount,
      String author,
      Long authorAccount,
      String keyIngredient) {
    this.recipeNum = recipeNum;
    this.kind = kind;
    this.recipeName = recipeName;
    this.picPath = picPath;
    MainIngredient = mainIngredient;
    this.accessories = accessories;
    this.measure = measure;
    this.viewCount = viewCount;
    this.collectCount = collectCount;
    this.author = author;
    this.authorAccount = authorAccount;
    this.keyIngredient = keyIngredient;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Recipe recipe = (Recipe) o;
    return recipeNum == recipe.recipeNum && Objects.equals(id, recipe.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, recipeNum);
  }
}
