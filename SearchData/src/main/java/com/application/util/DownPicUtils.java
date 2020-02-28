package com.application.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author PG
 */
public class DownPicUtils {

    /**
     * 下载图片
     */
    public  static void downFile(String url, String savePath, String file) throws Exception {
        URL url2 = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        File file1 = new File(savePath);
        if (!file1.exists())
            file1.mkdir();
        String saveFilePath = savePath + File.separator + file;
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);
        int byteRead = -1;
        byte[] buffer = new byte[4096];
        while ((byteRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteRead);
        }
        outputStream.close();
        inputStream.close();
    }
}
