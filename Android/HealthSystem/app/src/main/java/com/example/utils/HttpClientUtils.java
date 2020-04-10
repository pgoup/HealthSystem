package com.example.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.text.LineBreaker;
import android.net.IpPrefix;
import android.telecom.Call;

import com.alibaba.fastjson.JSON;
import com.example.mime.entity.UserManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


/**
 * 请求服务端的工具类，对整个项目的请求进行统一操作
 */
public class HttpClientUtils {
    private static final String IP = "192.168.43.194";
    private static final String PORT = "8080";
    public static final String HTTP_REQUEST_ERROR = "请求出现异常，请检查网络是否连接或重新操作";
    public static final String serverUrl = "http://" + IP + ":" + PORT + "/";
    static String result;
    private static final int TIME_OUT = 5000;
    /**
     * 请求图片
     */
    public static String httpGetPic(String url, String param) {
        final String source = url;
        final String params = param;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(source);
                    HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        Bitmap pic = BitmapFactory.decodeStream(inputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return result;
    }

    /**
     * 发送post请求，请求数据
     *
     * @param url
     * @param params
     * @return
     */
    public static String httpPostRequest(String url, String params) {
        final String resource = url;
        final String data = params;
        //   System.out.println("链接为：" + resource);

        try {
            Callable thread = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    HttpURLConnection connection = (HttpURLConnection) new URL(resource).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    int code = connection.getResponseCode();
                    if (connection.getResponseCode() == 200) {
                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream message = new ByteArrayOutputStream();
                        int length;
                        byte[] buffer = new byte[1024];
                        while ((length = inputStream.read(buffer)) != -1) {
                            message.write(buffer, 0, length);
                        }
                        inputStream.close();
                        message.close();
                        return new String(message.toByteArray());
                    } else {
                        System.out.println("请求的code为：0" + connection.getResponseCode());
                        return null;
                    }
                }
            };
            FutureTask<String> resultThread = new FutureTask<>(thread);
            new Thread(resultThread).start();
            return resultThread.get();
        } catch (Exception e) {
            System.out.println("An exception occurred for the HTTP post request");

            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向服务端发送图片等参数
     *
     * @param files
     * @param requestUrl
     * @param params
     * @param imageName
     * @return
     */
    public static String uploadImage(List<Bitmap> files, String requestUrl, Map<String, String> params, String imageName) {
        String result = null;
        //边界标识，随机生成
        final String boundary = UUID.randomUUID().toString();
        final String prefix = "--";
        final String lineEnd = "\r\n";
        //内容类型，表单类型数据
        final String contentType = "multipart/form-data";
        //显示进度框
        //show
        final String url1 = requestUrl;
        final List<Bitmap> requestFiles = files;
        final String picName = imageName;
        final Map<String, String> requestParams = params;
        try {
            Callable thread = new Callable() {
                @Override
                public Object call() throws Exception {
                    URL url = new URL(url1);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(TIME_OUT);
                    connection.setConnectTimeout(TIME_OUT);
                    //允许输入流
                    connection.setDoInput(true);
                    //允许输出流
                    connection.setDoOutput(true);
                    //不允许使用缓存
                    connection.setUseCaches(false);
                    //请求方式
                    connection.setRequestMethod("POST");
                    //设置编码
                    connection.setRequestProperty("Charset", "utf-8");
                    connection.setRequestProperty("connection", "keep-alive");
                    connection.setRequestProperty("X-bocang-Authorization", "token");
                    connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                    connection.setRequestProperty("Content-Type", contentType + ";boundary=" + boundary);
                    DataOutputStream data = new DataOutputStream(connection.getOutputStream());
                    StringBuffer stringBuffer;
                    String param = "";
                    if (requestParams != null && requestParams.size() > 0) {
                        Iterator<String> keys = requestParams.keySet().iterator();
                        while (keys.hasNext()) {
                            stringBuffer = new StringBuffer();
                            String key = keys.next();
                            String value = requestParams.get(key);
                            stringBuffer.append(prefix).append(boundary).append(lineEnd);
                            stringBuffer.append("Content-Disposition: form-data;name=\"")
                                    .append(key)
                                    .append("\"")
                                    .append(lineEnd)
                                    .append(lineEnd);
                            stringBuffer.append(value).append(lineEnd);
                            param = stringBuffer.toString();
                            //System.out.println("实参为：" + param);
                            data.write(param.getBytes());
                        }
                    }
                    /**
                     * name里面的值为服务器需要key  只有这个key次可以得到对应的文件
                     * filename是文件的名字，包含后缀名
                     */
                    for (int i = 0; i < requestFiles.size(); i++) {
                        stringBuffer = new StringBuffer();
                        stringBuffer.append(prefix).append(boundary).append(lineEnd);
                        String key = "num" + i;
                        //System.out.println("图片名为：" + key);
                        String filename = key + ".jpg";
                        stringBuffer.append("Content-Disposition:form-data;name=\"")
                                .append(key)
                                .append("\"")
                                .append(";filename=\"")
                                .append(filename)
                                .append("\"")
                                .append(lineEnd)
                                .append("Content-Type: image/pjpeg")
                                .append(lineEnd)
                                .append(lineEnd);
                        data.write(stringBuffer.toString().getBytes());
                        data.write(BitmapToInputStream(requestFiles.get(i)));
                        data.write(lineEnd.getBytes());

                    }
                    byte[] endData = (prefix + boundary + prefix + lineEnd).getBytes();
                    data.write(endData);
                    data.flush();

                    //   System.out.println("照片为：" + stringBuffer.toString());
                        /*InputStream inputStream = BitmapToInputStream(requestFiles.get(i));
                        byte[] bytes = new byte[1024];
                        int len;
                        while ((len = inputStream.read(bytes)) != -1) {
                            data.write(bytes, 0, len);
                        }
                        inputStream.close();*/

                    /**
                     * 获取响应码  成功获取响应流
                     */
                    int res = connection.getResponseCode();
                    if (res == 200) {
                        InputStream inputStream = connection.getInputStream();
                        StringBuffer stringBuffer1 = new StringBuffer();
                        int ss;
                        while ((ss = inputStream.read()) != -1) {
                            stringBuffer1.append((char) ss);
                        }
                        return stringBuffer1.toString();
                    } else {
                        System.out.println("请求失败");
                    }
                    return "";
                }
            };
            FutureTask<String> resultThread = new FutureTask<>(thread);
            new Thread(resultThread).start();
            return resultThread.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将bitmap转换为inputstream
     *
     * @param bitmap
     * @return
     */
    private static byte[] BitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 65, byteArray);
        // InputStream inputStream = new ByteArrayInputStream(byteArray.toByteArray());
        return byteArray.toByteArray();
    }

}
