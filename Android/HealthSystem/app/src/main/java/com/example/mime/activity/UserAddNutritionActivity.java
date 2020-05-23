package com.example.mime.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.RecipeDetailActivity;
import com.example.entity.RecipeItemClient;
import com.example.entity.UserAddNutrition;
import com.example.entity.UserInfoClient;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.List;

public class UserAddNutritionActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText minCountTextView;
    private EditText maxCountTextView;
    private String nutritionName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_user_add_nutrition);
        findViewById(R.id.user_add_nutrition_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner = findViewById(R.id.mime_user_add_nutrition_spinner);
        minCountTextView = findViewById(R.id.user_add_nutrition_min_count);
        minCountTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        maxCountTextView = findViewById(R.id.user_add_nutrition_max_count);
        maxCountTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        List<String> nutritions = new ArrayList<>();
        initNutrition(nutritions);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, nutritions);
        spinner.setAdapter(adapter1);
        spinner.setDropDownHorizontalOffset(1000);
        spinner.setGravity(Gravity.CENTER);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nutritionName = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        findViewById(R.id.mime_user_add_nutrition_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float min, max;
                try {
                    min = Float.valueOf(minCountTextView.getText().toString());
                    max = Float.valueOf(maxCountTextView.getText().toString());
                    postRequest(nutritionName, min, max);
                } catch (Exception e) {
                    InternetTooltip.tip(UserAddNutritionActivity.this, "最大值或最小值格式不对，请重新输入！");
                }
            }
        });
        TextView textView = findViewById(R.id.mime_user_add_nutrition_suggestion_count);
        textView.setText("单位提示：热量单位为大卡，脂肪、蛋白质、碳水化合物等为克，胆固醇、维生素B-E、钾、钙、钠、镁、磷、铁、锌、铜、锰、碘等元素单位为毫克、维生素A单位为微克。\n");
        textView.setGravity(Gravity.CENTER);
    }

    private void initNutrition(List<String> nutritions) {
        nutritions.add("热量");
        nutritions.add("蛋白质");
        nutritions.add("脂肪");
        nutritions.add("碳水化合物");
        nutritions.add("胆固醇");
        nutritions.add("钙");
        nutritions.add("铁");
    }

    private void postRequest(String name, float min, float max) {
        String param = "name=" + name + "&min=" + min + "&max=" + max + "&userAccount=" + UserManager.getInstance().getUserInfo(UserAddNutritionActivity.this).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "saveUserNutrition", param);
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        if (result.equals("false"))
            InternetTooltip.tip(this, "提交失败，请检查是否已添加该营养元素！");
        else {
            Bundle bundle = new Bundle();
            bundle.putString("name",name );
            bundle.putString("min",String.valueOf(min));
            bundle.putString("max",String.valueOf(max));
            Intent intent = new Intent();
            intent.setAction("AddNutritionAction");
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(UserAddNutritionActivity.this).sendBroadcast(intent);

            InternetTooltip.tip(this, "提交成功！");

        }
        return;
    }
}
