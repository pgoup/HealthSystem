package com.application.entity.server;

import lombok.Data;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.Set;

/** @author PG 健康饮食类型，有针对性的类型，如孕妇，高中生等等 */
@Entity
@Data
public class HealthDiet {
  @Id
  @Column(name = "health_diet_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 饮食编号 */
  @Column(unique = true, name = "health_diet_number")
  private Integer number;

  /** 饮食类型 有人群膳食、疾病调理、功能性调理、肝脏调理 */
  private String dietKind;

  /** 健康饮食的对象 */
  @Column(name = "health_diet_name", unique = true)
  private String healthDietName;

  /** 饮食方式 */
  private String dietMethod;

  /** 适宜食材 */
  private String suitable;

  /** 适宜的具体食材 */
  @Column(name = "suitable_foods", columnDefinition = "LONGTEXT")
  private String suitableFoods;

  /** 禁忌食材 */
  private String taboo;

  /** 禁忌的具体食材 */
  @Column(name = "taboo_foods", columnDefinition = "LONGTEXT")
  private String tabooFoods;

  // 食谱类别的权值
  private int weight;

  public HealthDiet() {}

  public HealthDiet(
      Integer number,
      String dietKind,
      String healthDietName,
      String dietMethod,
      String suitable,
      String suitableFoods,
      String taboo,
      String tabooFoods) {
    this.number = number;
    this.dietKind = dietKind;
    this.healthDietName = healthDietName;
    this.dietMethod = dietMethod;
    this.suitable = suitable;
    this.suitableFoods = suitableFoods;
    this.taboo = taboo;
    this.tabooFoods = tabooFoods;
  }
}
