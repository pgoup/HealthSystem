package com.example.mime.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.mime.MimeLoginedFragment;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MimeSelectTagsActivity extends AppCompatActivity {

    //食谱类别相关控件
    private Spinner firstTagSpinner;
    private Spinner secondTagSpinner;
    //所有的第二个spinner集合
    private List<Spinner> tagSpinners = new ArrayList<>();

    private Set<String> selectedTags = new HashSet<>();
    private LinearLayout addTagLayout;
    private List<LinearLayout> tagLayouts = new ArrayList<>();
    //所有的食谱类型
    private List<String> allRecipeKinds = new ArrayList<>();
    private static final String COULD_NOT_DELETE_MESSAGE = "至少保留一项，无法继续删除！";
    //所有的标签
    private Set<String> tagSet = new HashSet<>();

    private String firstSpinnerValue;
    private String secondValue;
    private List<String> firstTags = new ArrayList<>();
    private Map<String, List<String>> results = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mime_add_tags);
        firstTagSpinner = findViewById(R.id.mime_first_tag_spinner);
        secondTagSpinner = findViewById(R.id.mime_second_tag_spinner);
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getAllTags", "");
        JSONObject object = JSONObject.parseObject(result);
        results = JSONObject.toJavaObject(object, Map.class);
        Iterator iterator = results.keySet().iterator();
        while (iterator.hasNext())
            firstTags.add(iterator.next().toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MimeSelectTagsActivity.this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, firstTags);
        firstTagSpinner.setAdapter(adapter);

        firstTagSpinner.setDropDownHorizontalOffset(1000);
        firstTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstSpinnerValue = (String) firstTagSpinner.getSelectedItem();
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MimeSelectTagsActivity.this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, results.get(firstSpinnerValue));
                secondTagSpinner.setAdapter(adapter2);
                secondTagSpinner.setDropDownHorizontalOffset(1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String values = bundle.get("value").toString();
        String[] value = values.split("&&");
        for (String va : value) {
            tagSet.add(va);
        }
        tagSpinners.add(secondTagSpinner);
        addTagLayout = findViewById(R.id.mime_add_tag_layout);
        Button addButton = findViewById(R.id.mime_add_tag_spinner_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTagLayout();
            }
        });
        Button deleteButton = findViewById(R.id.mime_delete_tag_spinner_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagSpinners.size() > 1) {
                    addTagLayout.removeView(tagLayouts.get(tagLayouts.size() - 1));
                    tagSpinners.remove(tagSpinners.size() - 1);
                    tagLayouts.remove(tagLayouts.size() - 1);
                } else {
                    InternetTooltip.tip(MimeSelectTagsActivity.this, COULD_NOT_DELETE_MESSAGE);
                }
            }
        });
        findViewById(R.id.mime_add_tags_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < tagSpinners.size(); i++) {
                    selectedTags.add(tagSpinners.get(i).getSelectedItem().toString());
                }
                StringBuilder values = new StringBuilder();
                Iterator<String> iterator = selectedTags.iterator();
                while (iterator.hasNext()) {
                    String value = iterator.next();
                    values.append(value).append("&&");
                    tagSet.add(value);
                }
                String param = "userAccount=" + UserManager.getInstance().getUserInfo(MimeSelectTagsActivity.this).getAccount() + "&tags=" + values.toString();
                String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "addUserTags", param);
                // System.out.println("返回的结果为：" + result);
                if (result == null) {
                    InternetTooltip.tip(MimeSelectTagsActivity.this, HttpClientUtils.HTTP_REQUEST_ERROR);
                } else {
                    MimeLoginedFragment.refresh(tagSet);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MimeSelectTagsActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("添加成功！");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();


                }
            }
        });


    }

    private void addTagLayout() {
        LinearLayout linearLayout = new LinearLayout(MimeSelectTagsActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // layoutParam.width = getResources().getDisplayMetrics().widthPixels / 2;
        LinearLayout linearLayout1 = new LinearLayout(MimeSelectTagsActivity.this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(layoutParams1);

        LinearLayout linearLayout2 = new LinearLayout(MimeSelectTagsActivity.this);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(layoutParams1);

        final Spinner firstSpinner = new Spinner(this);
        Iterator iterator = results.keySet().iterator();
        List<String> firstValues = new ArrayList<>();
        while (iterator.hasNext())
            firstValues.add(iterator.next().toString());
        firstSpinner.setDropDownHorizontalOffset(1000);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, firstValues);
        firstSpinner.setAdapter(adapter1);
        firstSpinner.setLayoutParams(layoutParams2);
        firstSpinner.setDropDownHorizontalOffset(1000);

        final Spinner secondSpinner = new Spinner(this);
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String firstSpinnerText = firstSpinner.getSelectedItem().toString();
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MimeSelectTagsActivity.this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, results.get(firstSpinnerText));
                secondSpinner.setAdapter(adapter2);
                secondSpinner.setDropDownHorizontalOffset(1000);
                secondSpinner.setLayoutParams(layoutParams2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        tagSpinners.add(secondSpinner);
       /* linearLayout.addView(firstSpinner);
        linearLayout.addView(secondSpinner);*/
        linearLayout1.addView(firstSpinner);
        linearLayout2.addView(secondSpinner);
        linearLayout.addView(linearLayout1);
        linearLayout.addView(linearLayout2);
        tagLayouts.add(linearLayout);
        addTagLayout.addView(linearLayout);
    }
}
