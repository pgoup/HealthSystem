package com.application.entity.client;

import lombok.Data;

/** @author PG */
@Data
public class RecipePicAndNameClient {
  private String name;
  private byte[] pic;

  public RecipePicAndNameClient(String name, byte[] pic) {
    this.name = name;
    this.pic = pic;
  }
}
