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
import com.example.common.RecipeDetailActivity;
import com.example.entity.FoodClient;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;

public class FoodNutritionActivity extends AppCompatActivity {
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
            InternetTooltip.tip(FoodNutritionActivity.this, HttpClientUtils.HTTP_REQUEST_ERROR);
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
        foodNutritionIngredient.setOrientation(LinearLayout.VERTICAL);
        String[] values = nutritionalIngredient.split("##");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < values.length; i++) {
            String[] vs = values[i].split("&&");
            if (vs.length == 0) continue;
            LinearLayout linearLayout = new LinearLayout(FoodNutritionActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(FoodNutritionActivity.this);
            textView.setText(vs[0]);
            textView.setLayoutParams(layoutParams1);
            TextView textView1 = new TextView(FoodNutritionActivity.this);
            textView1.setText(vs[1]);
            textView1.setLayoutParams(layoutParams1);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView1.setGravity(View.TEXT_ALIGNMENT_CENTER);
            linearLayout.addView(textView);
            linearLayout.addView(textView1);
            foodNutritionIngredient.addView(linearLayout);
        }
    }
}
