package com.application.dao;

import com.application.entity.server.UserConcernedUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

/** @author PG */
@Repository
public interface UserConcernedUserDao extends JpaRepository<UserConcernedUser, Long> {
  @Modifying
  @Transactional
  @Query(
      value =
          "delete from user_concerned_user where user_account = ?1 and concerned_user_account = ?2",
      nativeQuery = true)
  void deleteByAccountAndConcernedAccount(Long userAccount, Long concernedUserAccount);

  @Query(
      value =
          "select id from user_concerned_user where user_account = ?1 and concerned_user_account = ?2 limit 1 ",
      nativeQuery = true)
  Long getIdByUserAccountAndConcernedAccount(Long userAccount, Long concernedAccount);

  @Query(
      value = "select concerned_user_account from user_concerned_user where user_account =?1",
      countQuery =
          "select count(concerned_user_account) from user_concerned_user where user_account =?1",
      nativeQuery = true)
  List<BigInteger> getConcernedUserAccount(Long userAccount, Pageable pageable);

  @Query(
          value = "select user_account from user_concerned_user where concerned_user_account =?1",
          countQuery =
                  "select count(user_account) from user_concerned_user where concerned_user_account =?1",
          nativeQuery = true)
  List<BigInteger> getFanUserAccount(Long concernedAccount,Pageable pageable);
}
