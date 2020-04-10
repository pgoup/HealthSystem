package com.application.util;

import com.application.entity.client.RecipeItemClient;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/** @author PG */
public class ConvertUtils {

  private static final String NO_PIC_PATH = "C:\\Users\\PG\\Desktop\\picture\\nopic.jpg";

  /** 将食谱显示的简要进行格式的转换 */
  public static List<RecipeItemClient> recipesConvertToContentItemClient(List<Object[]> results) {
    List<RecipeItemClient> clients = new ArrayList<>();
    for (Object[] result : results) {
      clients.add(objectsToContentItemClient(result));
    }
    return clients;
  }

  public static RecipeItemClient objectsToContentItemClient(Object[] result) {
    return new RecipeItemClient(
        result[5].toString(),
        result[0].toString(),
        picConvertToByte(result[1].toString().equals("") ? NO_PIC_PATH : result[1].toString()),
        result[2].toString(),
        result[3] == null ? 0 : (int) result[3],
        result[4] == null ? 0 : (int) result[4]);
  }

  /** 将图片地址转换为byte数组 */
  public static byte[] picConvertToByte(String picPath) {
    FileInputStream fileInputStream;
    byte[] pic = null;
    if (picPath == null) return null;
    if (!picPath.equals("无图片")) {
      try {
        fileInputStream = new FileInputStream(new File(picPath));
        pic = new byte[fileInputStream.available()];
        fileInputStream.read(pic);
        fileInputStream.close();
      } catch (Exception e) {
        System.out.println("pic convert to byte has occurred an exception");
      }
    }
    return pic;
  }

  public static byte[] getNoPic() {
    return picConvertToByte(NO_PIC_PATH);
  }
}
