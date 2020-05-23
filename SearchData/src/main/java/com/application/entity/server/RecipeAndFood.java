package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/**
 * @author PG
 */
@Entity
@Data
public class RecipeAndFood {
    @Id
    @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long foodNum;
    private Long recipeNum;

    public RecipeAndFood() {
    }
}
