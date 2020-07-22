package com.example.mime.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.entity.RecipeNutritionClient;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;

public class RecipeNutritionActivity extends AppCompatActivity {
    private TextView recipeName;
    private ImageView recipeImage;
    private LinearLayout recipeNutritionIngredient;

    private RecipeNutritionClient recipeNutritionClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recipe_nutrition_content);
        recipeName = findViewById(R.id.recipe_nutrition_name);
        recipeImage = findViewById(R.id.recipe_nutrition_image);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        initData(bundle.get("recipeNum").toString());
        recipeName = findViewById(R.id.recipe_nutrition_name);
        recipeName.setText(recipeNutritionClient.getRecipeName());
        recipeNutritionIngredient = findViewById(R.id.recipe_nutritional_ingredient);
        initView(recipeNutritionClient.getNutritions());
        findViewById(R.id.recipe_nutrition_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recipeImage = findViewById(R.id.recipe_nutrition_image);
        Bitmap bitmap = PicUtils.byteConvertToBitmap(recipeNutritionClient.getRecipeImage());
        RequestOptions options = new RequestOptions().error(R.drawable.image1).bitmapTransform(new RoundedCorners(30));//图片圆角为30
        Glide.with(this).load(Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, null, null))) //图片地址
                .apply(options)
                .into(recipeImage);
        recipeImage.setImageBitmap(bitmap);

    }

    private void initView(String nutritionalIngredient) {
        String[] values = nutritionalIngredient.split("##");
        int len = values.length / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < len; i++) {
            LinearLayout linearLayout = new LinearLayout(RecipeNutritionActivity.this);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(RecipeNutritionActivity.this);
            textView.setText(values[2 * i].replaceAll("&", " "));
            textView.setGravity(Gravity.LEFT);

            linearLayout.addView(textView);
            TextView textView1 = new TextView(RecipeNutritionActivity.this);
            textView1.setText(values[2 * i + 1].replaceAll("&", " "));
            textView1.setGravity(Gravity.RIGHT);
            layoutParams.gravity = Gravity.RIGHT;
            textView1.setLayoutParams(layoutParams);
            linearLayout.addView(textView1);
            recipeNutritionIngredient.addView(linearLayout);
        }
        if (values.length > len * 2) {
            LinearLayout linearLayout = new LinearLayout(RecipeNutritionActivity.this);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(RecipeNutritionActivity.this);
            textView.setText(values[2 * len]);
            textView.setGravity(Gravity.LEFT);
            linearLayout.addView(textView);
            recipeNutritionIngredient.addView(linearLayout);
        }
    }


    private void initData(String recipeNum) {
        final String num = recipeNum;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initContent(num);
            }
        }).run();
    }

    /**
     * 初始化页面的内容
     *
     * @param recipeNum
     */
    private void initContent(String recipeNum) {
        String param = "recipeNum=" + recipeNum;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getRecipeNutritionByRecipeNum", param);
        if (result == null) {
            InternetTooltip.tip(RecipeNutritionActivity.this, "请求出现异常，请检查是否网络未连接");
            return;
        }
        JSONObject object = JSONObject.parseObject(result);
        recipeNutritionClient = JSONObject.toJavaObject(object, RecipeNutritionClient.class);
    }
}
