package com.example.common;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.entity.RecipeClient;
import com.example.entity.RecipeMeasureItem;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity {
    private SimpleDraweeView recipeImage;
    private TextView recipeName;
    private String recipeNum;
    private TextView recipeAuthor;
    private SimpleDraweeView recipeAuthorImage;
    private TextView recipeKind;
    private LinearLayout recipeMainIngredient;
    private LinearLayout recipeAccessories;
    private LinearLayout measureLayout;
    private TextView recipeViewCount;
    private TextView recipeCollectCount;
    private RecipeClient recipe;
    private LinearLayout recipeLayout;
    private Button collectButton;
    private Button concernButton;
    private ScrollView scrollView;
    private TextView recipeAuthorAccount;
    private static boolean isConcerned;
    private static boolean isCollected;


    private AlertDialog alertDialog;
    private TextView tipDialogText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recipe_detail);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecipeDetailActivity.this);
        View view = View.inflate(RecipeDetailActivity.this, R.layout.tip_item, null);
        tipDialogText = view.findViewById(R.id.tip_item_text);
        alertDialog = dialogBuilder.create();
        alertDialog.setView(view);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeNum = (String) bundle.get("recipeNum");
        initData(recipeNum);
        findViewById(R.id.content_detail_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.content_detail_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        scrollView = findViewById(R.id.recipe_detail_scroll_view);
        collectButton = findViewById(R.id.recipe_detail_collect_button);
        isCollected = recipe.isCollected();
        changeState(isCollected, "1已收藏", "1收藏", "收藏", (int) collectButton.getTextSize());
        initView();
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
        String userAccount = "";
        if (UserManager.getInstance().hasUserInfo(this)) {
            userAccount = UserManager.getInstance().getUserInfo(this).getAccount().toString();
        }
        String param = "recipeNum=" + recipeNum + "&userAccount=" + userAccount;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getRecipeByNum", param);
        if (result == null) {
            InternetTooltip.tip(RecipeDetailActivity.this, "请求出现异常，请检查是否网络未连接");
            return;
        }
        JSONObject object = JSONObject.parseObject(result);
        recipe = JSONObject.toJavaObject(object, RecipeClient.class);
        isConcerned = recipe.isConcerned();
        isCollected = recipe.isCollected();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.recipe_detail_content, null);
        recipeLayout = view.findViewById(R.id.recipe_layout);
        recipeImage = view.findViewById(R.id.detail_recipe_image);
        recipeName = view.findViewById(R.id.detail_recipe_name);
        recipeKind = view.findViewById(R.id.detail_recipe_kind);
        recipeMainIngredient = view.findViewById(R.id.detail_recipe_main_ingredient);
        recipeAuthor = view.findViewById(R.id.recipe_detail_author_name);
        recipeAuthorImage = view.findViewById(R.id.recipe_detail_author_image);
        concernButton = view.findViewById(R.id.recipe_detail_author_concern);
        isConcerned = recipe.isConcerned();
        changeState(isConcerned, "已关注", "关注", "关注", (int) concernButton.getTextSize());
        recipeAccessories = view.findViewById(R.id.detail_recipe_accessories);
        recipeViewCount = view.findViewById(R.id.detail_recipe_view_count);
        recipeCollectCount = view.findViewById(R.id.detail_recipe_collect_count);
        recipeAuthorAccount = view.findViewById(R.id.recipe_author);
        measureLayout = view.findViewById(R.id.detail_recipe_measure);
        measureLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Bitmap bitmap = PicUtils.byteConvertToBitmap(recipe.getPic());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(new BitmapDrawable(bitmap))
                .build();
        RoundingParams params = RoundingParams.fromCornersRadius(30f);
        params.setCornersRadius(30);
        params.setRoundAsCircle(false);
        hierarchy.setRoundingParams(params);
        recipeImage.setHierarchy(hierarchy);
        recipeName.setText(recipe.getRecipeName());
        recipeKind.setText(recipe.getKind());
        recipeAuthor.setText(recipe.getAuthor());
        Bitmap bitmap1 = PicUtils.byteConvertToBitmap(recipe.getAuthorImage());
        GenericDraweeHierarchyBuilder builder1 =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy1 = builder1
                .setPlaceholderImage(new BitmapDrawable(bitmap1))
                .build();
        RoundingParams params1 = RoundingParams.fromCornersRadius(5f);
        params1.setRoundAsCircle(true);
        hierarchy1.setRoundingParams(params1);
        recipeAuthorImage.setHierarchy(hierarchy1);
        createAccessory("main", recipe.getMainIngredient());
        createAccessory("accessory", recipe.getAccessories());
        //recipeAccessories.setText(recipe.getAccessories());
        recipeViewCount.setText(recipe.getViewCount().toString());
        recipeAuthorAccount.setText(recipe.getAuthor());
        //System.out.println("初始化视图时收藏的次数为：" + recipe.getCollectNum());
        recipeCollectCount.setText(String.valueOf(recipe.getCollectNum()));
        //初始化方法布局
        initMeasure(recipe.getMeasure(), measureLayout);
        scrollView.addView(recipeLayout);
        //收藏按钮监听器

        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserManager.getInstance().hasUserInfo(RecipeDetailActivity.this)) {
                    InternetTooltip.tip(RecipeDetailActivity.this, "您还未登录，请先登录");
                    return;
                }
                String param = "userAccount=" + UserManager.getInstance().getUserInfo(RecipeDetailActivity.this).getAccount() + "&collectRecipeNum=" + recipeNum + "&isCollected=" + isCollected;
                String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "updateUserCollect", param);
                if (result == null) {
                    InternetTooltip.tip(RecipeDetailActivity.this, HttpClientUtils.HTTP_REQUEST_ERROR);
                } else if (result.equals("false")) {
                    InternetTooltip.tip(RecipeDetailActivity.this, "操作失败！");
                } else {
                    isCollected = !isCollected;
                    changeState(isCollected, "1已收藏", "1收藏", "收藏", (int) concernButton.getTextSize());
                    Long num = Long.valueOf(recipeCollectCount.getText().toString());
                    refresh(isCollected ? num - 1 : num + 1);
                    InternetTooltip.tip(RecipeDetailActivity.this, "操作成功！");
                }
            }
        });
        concernButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserManager.getInstance().hasUserInfo(RecipeDetailActivity.this)) {
                    InternetTooltip.tip(RecipeDetailActivity.this, "您还未登录，请先登录");
                    return;
                }
                String param = "userAccount=" + UserManager.getInstance().getUserInfo(RecipeDetailActivity.this).getAccount() + "&concernedUserAccount=" + recipe.getAuthorAccount() + "&isConcerned=" + isConcerned;
                String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "updateUserConcern", param);
                if (result == null) {
                    InternetTooltip.tip(RecipeDetailActivity.this, HttpClientUtils.HTTP_REQUEST_ERROR);
                } else if (result.equals("false")) {
                    InternetTooltip.tip(RecipeDetailActivity.this, "操作失败！");
                } else {
                    InternetTooltip.tip(RecipeDetailActivity.this, "操作成功！");
                    Bundle bundle = new Bundle();
                    isConcerned = !isConcerned;
                    bundle.putBoolean("concern", isConcerned);
                    Intent intent = new Intent();
                    intent.setAction("ConcernAction");
                    intent.putExtras(bundle);
                    LocalBroadcastManager.getInstance(RecipeDetailActivity.this).sendBroadcast(intent);
                    //更改关注按钮状态
                    changeState(isConcerned, "已关注", "关注", "关注", (int) concernButton.getTextSize());
                }
            }
        });
    }

    /**
     * 初始化方法，将方法步骤填进相应的页面
     *
     * @param measureItems
     */
    private void initMeasure(List<RecipeMeasureItem> measureItems, LinearLayout measureLayout) {
        PicUtils.saveRecipePic(measureItems);
        for (RecipeMeasureItem measureItem : measureItems) {
            TextView textView = new TextView(this);
            textView.setText(measureItem.getMeasure());
            measureLayout.addView(textView);
            if (measureItem.getPic() != null) {
                Bitmap pic = PicUtils.byteConvertToBitmap(measureItem.getPic());
                ImageView picView = new ImageView(this);
                picView.setImageBitmap(pic);
                picView.setAdjustViewBounds(true);
                Display display = getWindowManager().getDefaultDisplay();
                picView.setMaxWidth(display.getWidth());
                picView.setMaxHeight(display.getWidth());
                measureLayout.addView(picView);
            }
        }
    }

    private void createAccessory(String kind, String accessories) {
        if (accessories.isEmpty()) return;
        String[] accessory = accessories.split("##");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < accessory.length; i++) {
            LinearLayout linearLayout = new LinearLayout(RecipeDetailActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(layoutParams);
            String[] values = accessory[i].split("&&");
            if (values.length == 0) return;
            TextView textView = new TextView(RecipeDetailActivity.this);
            textView.setText(values[0]);
            textView.setLayoutParams(layoutParams1);
            TextView textView1 = new TextView(RecipeDetailActivity.this);
            textView1.setText(values[1]);
            textView1.setLayoutParams(layoutParams1);
            linearLayout.addView(textView);
            linearLayout.addView(textView1);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView1.setGravity(View.TEXT_ALIGNMENT_CENTER);
            if (kind.equals("main"))
                recipeMainIngredient.addView(linearLayout);
            else
                recipeAccessories.addView(linearLayout);
        }
    }

    //更新页面
    private void refresh(final long collectCount) {
        Handler mTimeHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    recipeCollectCount.setText(String.valueOf(collectCount));
                }
            }
        };
        mTimeHandler.sendEmptyMessageDelayed(0, 10);
    }

    //更改收藏和关注按钮的状态
    private void changeState(boolean state, String isMessage, String notMessage, String button, int textSize) {
        String concernMessage = state ? isMessage : notMessage;
        if (state) {
            if (button.equals("收藏")) {
                SpannableString spannableString = new SpannableString(concernMessage);
                Drawable drawable = getResources().getDrawable(R.drawable.like_select);
                drawable.setBounds(0, 0, textSize, textSize);
                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                collectButton.setText(spannableString);
                collectButton.setTextColor(getResources().getColor(R.color.colorRed));
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(10, Color.RED);
                drawable.setColor(getResources().getColor(R.color.colorWhiteSmoke));
                concernButton.setText(concernMessage);
                concernButton.setBackground(drawable);
                concernButton.setTextColor(getResources().getColor(R.color.colorRed));
            }
        } else {
            if (button.equals("收藏")) {
                SpannableString spannableString = new SpannableString(concernMessage);
                Drawable drawable = getResources().getDrawable(R.drawable.like);
                drawable.setBounds(0, 0, textSize + 5, textSize + 5);
                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                collectButton.setText(spannableString);
                collectButton.setTextColor(getResources().getColor(R.color.colorBlack));
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(10, getResources().getColor(R.color.colorWhiteSmoke));
                drawable.setColor(getResources().getColor(R.color.colorWhiteSmoke));
                concernButton.setText(concernMessage);
                concernButton.setBackground(drawable);
                concernButton.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        }
    }
}

