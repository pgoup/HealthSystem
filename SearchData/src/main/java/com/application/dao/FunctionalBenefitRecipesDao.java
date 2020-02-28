package com.application.dao;

import com.application.entity.server.FunctionalBenefitRecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author PG
 */
@Repository
public interface FunctionalBenefitRecipesDao extends JpaRepository<FunctionalBenefitRecipes, Long> {
}
