package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/** @author PG 食谱类型，例如家常菜，湖北菜等等 */
@Entity
@Data
public class RecipeKind {

  @Id
  @Column(name = "recipe_kind_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, name = "recipe_kind_num")
  private Integer recipeKindNum;

  @Column(name = "normal")
  private Integer normal;

  @Column(name = "recipe_kind_name", unique = true)
  private String kindName;

  private int weight;

  private String pic;

  public RecipeKind() {}

  public RecipeKind(Integer recipeKindNum, String kindName) {
    this.recipeKindNum = recipeKindNum;
    this.kindName = kindName;
  }
}
