package com.application.entity.server;

import lombok.Data;

import javax.persistence.*;

/** @author PG */
@Entity
@Data
public class UserConcernedUser {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userAccount;

  private Long concernedUserAccount;
}
