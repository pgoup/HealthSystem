package com.example.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.biometrics.BiometricManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.entity.RecipeMeasureItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 将byte格式的图片转换为bitmap格式的image
 */
public class PicUtils {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "\\measure\\";
    public static final String DEFAULT_POST_PIC_PATH = HttpClientUtils.serverUrl + "getPic";
    public static final String TAG = "PicUtils";

    /**
     * 拍照后的操作
     *
     * @param activity
     * @param imageUri    拍照后照片存储的路径
     * @param requestCode 调用系统相机请求码
     */
    public static void takePic(Activity activity, Uri imageUri, int requestCode) {
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //对目标应用临时授权该uri所代表的文件
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存到photo_file的uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    /**
     * 读取uri所在的图片
     *
     * @param uri     图片对应的uri
     * @param context
     * @return 图像的bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将食谱的图片进行保存
     *
     * @param measureItems
     */
    public static void saveRecipePic(List<RecipeMeasureItem> measureItems) {
        for (RecipeMeasureItem measureItem : measureItems) {
            saveMeasurePic(measureItem.getRecipeNum(), measureItem.getNum(), measureItem.getPic());
        }
    }


    /**
     * 将图片的制作步骤图片保存到本地
     *
     * @param recipeNum
     * @param num
     * @param pic
     * @return
     */
    public static boolean saveMeasurePic(long recipeNum, int num, byte[] pic) {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED))
            return false;
        try {
            File file = new File(DEFAULT_PATH + recipeNum + "\\" + num + ".png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(pic, 0, pic.length);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("保存图片出现异常");
            return false;
        }
        return true;
    }


    /**
     * 查看本地是否存在缓存，如果有直接在本地进行读取，不用请求服务器
     *
     * @param recipeNum 食谱编号
     * @param num       步骤标号
     */
    public static Bitmap getPicBitmap(long recipeNum, int num) {
        String path = DEFAULT_PATH + recipeNum + "\\" + num + ".png";
        File file = new File(path);
        byte[] pic = null;
        final String param = recipeNum + "_" + num;
        //请求服务器获取图片
        if (!file.isFile()) {
            try {
                Callable thread = new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        HttpURLConnection connection = (HttpURLConnection) new URL(DEFAULT_POST_PIC_PATH).openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                        if (connection.getResponseCode() == 200) {
                            InputStream inputStream = connection.getInputStream();
                            return new byte[inputStream.available()];
                        } else {
                            System.out.println("请求的code为：0" + connection.getResponseCode());
                            return null;
                        }
                    }
                };
                FutureTask<byte[]> resultThread = new FutureTask<>(thread);
                new Thread(resultThread).start();
                byte[] picture = resultThread.get();
                saveMeasurePic(recipeNum, num, picture);
                return byteConvertToBitmap(resultThread.get());
            } catch (Exception e) {
                System.out.println("请求服务器图片出现异常");
            }
        } else {
            try {
                InputStream stream = new FileInputStream(file);
                pic = new byte[stream.available()];
            } catch (Exception e) {
                System.out.println("获取本地图片出现异常");
            }
        }
        return byteConvertToBitmap(pic);
    }


    /**
     * 将图片的字节数组转换为bitmap图片格式
     *
     * @param pic
     * @return
     */
    public static Bitmap byteConvertToBitmap(byte[] pic) {
        final byte[] picPath = pic;
        Bitmap bitmap = null;
        try {
            bitmap = new Callable<Bitmap>() {
                @Override
                public Bitmap call() {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(picPath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inSampleSize = 2;
                    //return (new WeakReference<Bitmap>(BitmapFactory.decodeStream(inputStream, null, options))).get();
                    return (new WeakReference<>(BitmapFactory.decodeByteArray(picPath, 0, picPath.length))).get();
                    // return BitmapFactory.decodeByteArray(picPath, 0, picPath.length);
                }
            }.call();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}


