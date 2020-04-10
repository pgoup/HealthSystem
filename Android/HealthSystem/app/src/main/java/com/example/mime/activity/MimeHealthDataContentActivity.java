package com.example.mime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.entity.RecipeItemClient;
import com.example.entity.RecipeNutritionSuggestion;
import com.example.homepage.adapter.RecipeContentAdapter;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class MimeHealthDataContentActivity extends AppCompatActivity {
    private LinearLayout recipeNutritionIngredient;
    private List<RecipeItemClient> recipeItemClients = new ArrayList<>();
    private RecyclerView recipeRecycler;
    private RecipeContentAdapter recipeContentAdapter;
    private TextView suggestionText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_health_data_content_fragment);
        findViewById(R.id.mime_health_data_content_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeItemClients = JSONArray.parseArray(bundle.get("recipes").toString(), RecipeItemClient.class);
        recipeRecycler = findViewById(R.id.mime_health_data_content_recycler);
        recipeContentAdapter = new RecipeContentAdapter(this, recipeItemClients, 3);
        recipeRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        recipeRecycler.setItemAnimator(new DefaultItemAnimator());
        recipeRecycler.addItemDecoration(new SpacesItemDecoration());
        recipeRecycler.setAdapter(recipeContentAdapter);
        recipeRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recipeNutritionIngredient = findViewById(R.id.mime_health_data_detail_nutrition_layout);
        suggestionText = findViewById(R.id.mime_health_data_detail_suggestion);
        StringBuilder builder = new StringBuilder();
        for (RecipeItemClient itemClient : recipeItemClients) {
            builder.append(itemClient.getNum() + "&&");
        }
        RecipeNutritionSuggestion nutritionSuggestion = postRequest(builder.toString());
        initView(nutritionSuggestion);
    }

    private void initView(RecipeNutritionSuggestion nutritionSuggestion) {
        String nutritionalIngredient = nutritionSuggestion.getNutritions();
        suggestionText.setText(nutritionSuggestion.getSuggestions());
        String[] values = nutritionalIngredient.split("##");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < values.length; i++) {
            String[] vs = values[i].split("&&");
            if (vs.length == 0) continue;
            LinearLayout linearLayout = new LinearLayout(MimeHealthDataContentActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(MimeHealthDataContentActivity.this);
            textView.setText(vs[0]);
            textView.setLayoutParams(layoutParams1);
            TextView textView1 = new TextView(MimeHealthDataContentActivity.this);
            textView1.setText(vs[1]);
            textView1.setLayoutParams(layoutParams1);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView1.setGravity(View.TEXT_ALIGNMENT_CENTER);
            linearLayout.addView(textView);
            linearLayout.addView(textView1);
            recipeNutritionIngredient.addView(linearLayout);
        }
    }


    /**
     * 初始化页面的内容
     *
     * @param recipes
     */
    private RecipeNutritionSuggestion postRequest(String recipes) {
        String param = "recipes=" + recipes + "&userAccount=" + UserManager.getInstance().getUserInfo(this).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getRecipesNutritionByRecipeNums", param);
        if (result == null) {
            InternetTooltip.tip(
                    this, "请求出现异常，请检查是否网络未连接");
            return null;
        }
        JSONObject object = JSONObject.parseObject(result);
        return JSONObject.toJavaObject(object, RecipeNutritionSuggestion.class);
    }
}
