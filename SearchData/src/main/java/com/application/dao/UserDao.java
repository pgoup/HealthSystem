package com.application.dao;

import com.application.entity.client.UserInfoClient;
import com.application.entity.server.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserDao extends JpaRepository<UserInfo, Long> {
  @Query(value = "select user_account from user_info where user_account=?1", nativeQuery = true)
  String getByUserAccount(Long account);

  @Query(value = "select user_account from user_info order by id desc  limit 1", nativeQuery = true)
  Long getMaxAccount();

  @Query(value = "select * from user_info where user_name = ?1", nativeQuery = true)
  UserInfo getAccountByUserName(Long userName);

  @Query(
      value = "select user_account from user_info where user_name = ?1 order by id desc limit 1",
      nativeQuery = true)
  Long getUserAccountByName(String userName);

  @Query(
      value =
          "select user_account,user_name,pic,intro,fans,attentions  from user_info where user_account=?1 and password=?2 limit 1",
      nativeQuery = true)
  List<Object[]> getAllByAccountAndPass(Long userAccount, String password);

  @Query(value = "select * from user_info where user_account = ?1 limit 1", nativeQuery = true)
  UserInfo getUserInfoByAccount(Long userAccount);

  @Query(value = "select pic from user_info where user_account = ?1 limit 1", nativeQuery = true)
  String getPicPathByUserAccount(Long userAccount);

  @Modifying
  @Query(value = "update user_info set pic=?2 where user_account=?1", nativeQuery = true)
  @Transactional
  void updateUserImage(Long userAccount, String picPath);

  @Modifying
  @Query(value = "update user_info set intro=?2 where user_account=?1", nativeQuery = true)
  @Transactional
  void updateUserIntro(Long userAccount, String intro);

  @Query(value = "select * from user_info where user_account=?1 limit 1", nativeQuery = true)
  UserInfo getUserDetailInfo(Long userAccount);

  @Modifying
  @Query(value = "update user_info set user_name=?2 where user_account=?1", nativeQuery = true)
  @Transactional
  void updateUserName(Long userAccount, String userName);

  @Modifying
  @Query(value = "update user_info set height=?2 where user_account=?1", nativeQuery = true)
  @Transactional
  void updateUserHeight(Long userAccount, float userHeight);

  @Modifying
  @Query(value = "update user_info set weight=?2 where user_account=?1", nativeQuery = true)
  @Transactional
  void updateUserWeight(Long userAccount, float userWeight);

  @Query(value = "select weight from user_info where user_account=?1 limit 1", nativeQuery = true)
  Float getWeightByAccount(Long userAccount);
}
