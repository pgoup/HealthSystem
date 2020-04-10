package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG 食谱类别和食谱的多对多关系的实体类 */
@Data
@Entity(name = "recipes_and_kinds")
public class RecipeKindRecipes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "recipe_kind_num")
  private int recipeKindNum;

  @Column(name = "recipe_num")
  private Long recipeNum;

  public RecipeKindRecipes() {}

  public RecipeKindRecipes(int recipeKindNum, Long recipeNum) {
    this.recipeKindNum = recipeKindNum;
    this.recipeNum = recipeNum;
  }
}
