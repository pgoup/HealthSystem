package com.example.nutritionQuery;

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
import com.example.entity.FoodClient;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;

public class FoodDetailActivity extends AppCompatActivity {
    private ImageView foodImage;
    private TextView foodName;
    private TextView foodIntroduction;
    private TextView foodHealthWorth;
    private TextView foodBenefit;
    private TextView foodSuitablePeople;
    private TextView foodTabooPeople;
    private LinearLayout foodNutritionIngredient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.food_detail);
        findViewById(R.id.food_detail_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String foodName = (String) bundle.get("foodName");
        initData(foodName);
    }

    private void initData(String foodName) {
        String param = "foodName=" + foodName;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getFoodByName", param);
        if (result == null) {
            InternetTooltip.tooltip(FoodDetailActivity.this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        JSONObject object = JSONObject.parseObject(result);
        FoodClient foodClient = JSONObject.toJavaObject(object, FoodClient.class);
        initView(foodClient);

    }

    private void initView(FoodClient food) {
        foodImage = findViewById(R.id.food_image);
        Bitmap bitmap = PicUtils.byteConvertToBitmap(food.getPicPath());
        RequestOptions options = new RequestOptions().error(R.drawable.image1).bitmapTransform(new RoundedCorners(30));//图片圆角为30
        Glide.with(this).load(Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null))) //图片地址
                .apply(options)
                .into(foodImage);

        foodName = findViewById(R.id.food_name);
        foodName.setText(food.getFoodName());
        foodIntroduction = findViewById(R.id.food_introduction);
        foodIntroduction.setText(food.getIntroduction());
        foodHealthWorth = findViewById(R.id.food_health_worth);
        foodHealthWorth.setText(food.getHealthWorth());
        foodBenefit = findViewById(R.id.food_benefit);
        foodBenefit.setText(food.getBenefit());
        foodSuitablePeople = findViewById(R.id.food_suitable_people);
        foodSuitablePeople.setText(food.getSuitablePeople());
        foodTabooPeople = findViewById(R.id.food_taboo_people);
        foodTabooPeople.setText(food.getTabooPeople());
        foodNutritionIngredient = findViewById(R.id.food_nutritional_ingredient);
        initHealthContent(food.getNutritionalIngredient());
    }

    private void initHealthContent(String nutritionalIngredient) {
        String[] values = nutritionalIngredient.split("/");
        int len = values.length / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < len; i++) {
            LinearLayout linearLayout = new LinearLayout(FoodDetailActivity.this);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(FoodDetailActivity.this);
            textView.setText(values[2 * i]);
            textView.setGravity(Gravity.LEFT);

            linearLayout.addView(textView);
            TextView textView1 = new TextView(FoodDetailActivity.this);
            textView1.setText(values[2 * i + 1]);
            textView1.setGravity(Gravity.RIGHT);
            layoutParams.gravity = Gravity.RIGHT;
            textView1.setLayoutParams(layoutParams);
            linearLayout.addView(textView1);
            foodNutritionIngredient.addView(linearLayout);
        }
        if (values.length > len * 2) {
            LinearLayout linearLayout = new LinearLayout(FoodDetailActivity.this);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(FoodDetailActivity.this);
            textView.setText(values[2 * len]);
            textView.setGravity(Gravity.LEFT);
            linearLayout.addView(textView);
            foodNutritionIngredient.addView(linearLayout);
        }
    }
}
