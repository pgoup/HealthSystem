package com.application.dao;

import com.application.entity.server.FunctionalBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author PG
 */
@Repository
public interface FunctionalBenefitDao extends JpaRepository<FunctionalBenefit, Long> {
    @Query(value = "select * from functional_benefit where benefit_name=?1 limit 1", nativeQuery = true)
    FunctionalBenefit getByName(String name);

    @Query(value = "select benefit_num from functional_benefit where benefit_name=?1 limit 1", nativeQuery = true)
    Integer getNumByName(String name);

    @Query(value = "select benefit_num from functional_benefit  order by benefit_id desc limit 1", nativeQuery = true)
    Integer getNum();
}
