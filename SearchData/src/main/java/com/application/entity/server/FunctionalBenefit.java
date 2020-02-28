package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Data
public class FunctionalBenefit {
  @Id
  @Column(name = "benefit_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(unique = true, name = "benefit_name")
  private String benefitName;

  @Column(unique = true, name = "benefit_num")
  private Integer benefitNum;

  public FunctionalBenefit() {}

  public FunctionalBenefit(String benefitName, int benefitNum) {
    this.benefitName = benefitName;
    this.benefitNum = benefitNum;
  }
}
