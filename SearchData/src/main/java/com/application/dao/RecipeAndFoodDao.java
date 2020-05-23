package com.application.dao;

import com.application.entity.server.RecipeAndFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** @author PG */
@Repository
public interface RecipeAndFoodDao extends JpaRepository<RecipeAndFood, Long> {}
