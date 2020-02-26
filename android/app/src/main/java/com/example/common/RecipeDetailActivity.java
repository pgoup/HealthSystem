package com.example.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.util.ArrayList;
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
    private TextView recipeMeasure;
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

    private static final String IS_COLLECTED = "isCollected";
    private static final String COLLECT_RECIPE = "collectRecipe";
    private static final String CANCEL_COLLECT = "cancelCollect";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recipe_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeNum = (String) bundle.get("recipeNum");
        // System.out.println("跳转到详情页面的食谱编号为：" + recipeNum + "收藏的次数为：" + recipeCollectCount);
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
        System.out.println("进入页面后是否关注：" + isConcerned);
        changeState(isCollected, "1已收藏", "1收藏", R.drawable.menu_channel_hot, "收藏", (int) collectButton.getTextSize());
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
        String param = "recipeNum=" + recipeNum + "&userAccount=" + UserManager.getInstance().getUserInfo(RecipeDetailActivity.this).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/getRecipeByNum", param);
        if (result == null) {
            InternetTooltip.tooltip(RecipeDetailActivity.this, "请求出现异常，请检查是否网络未连接");
            return;
        }
        JSONObject object = JSONObject.parseObject(result);
        recipe = JSONObject.toJavaObject(object, RecipeClient.class);
        isConcerned = recipe.isConcerned();
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
        changeState(isConcerned, "1已关注", "1关注", R.drawable.menu_channel_hot, "关注", (int) concernButton.getTextSize());
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
        System.out.println("初始化视图时收藏的次数为：" + recipe.getCollectNum());
        recipeCollectCount.setText(String.valueOf(recipe.getCollectNum()));
        //初始化方法布局
        initMeasure(recipe.getMeasure(), measureLayout);
        scrollView.addView(recipeLayout);
        //收藏按钮监听器
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("收藏按钮被点击");
                if (!UserManager.getInstance().hasUserInfo(RecipeDetailActivity.this)) {
                    InternetTooltip.tooltip(RecipeDetailActivity.this, "您还未登录，请先登录");
                    return;
                }
                boolean result = collectRecipe(UserManager.getInstance().getUserInfo(RecipeDetailActivity.this).getAccount(), recipeNum, IS_COLLECTED);
                if (result) {
                    InternetTooltip.tooltip(RecipeDetailActivity.this, "已取消收藏！");
                    refresh(recipe.getCollectNum() - 1);

                    return;
                } else {
                    result = collectRecipe(UserManager.getInstance().getUserInfo(RecipeDetailActivity.this).getAccount(), recipeNum, COLLECT_RECIPE);
                    if (result) {
                        InternetTooltip.tooltip(RecipeDetailActivity.this, "已收藏成功");
                        //recipeCollectCount.setText(Integer.valueOf(recipeCollectCount.getText().toString()) + 1);
                        refresh(Long.valueOf(recipeCollectCount.getText().toString()) + 1);
                        //更改按钮状态
                        isCollected = !isConcerned;
                        return;
                    } else {
                        InternetTooltip.tooltip(RecipeDetailActivity.this, "收藏失败，请检查网络是否出现问题");
                        return;
                    }
                }
            }
        });
        concernButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("关注的用户编号为：" + recipe.getAuthorAccount());
                String param = "userAccount=" + UserManager.getInstance().getUserInfo(RecipeDetailActivity.this).getAccount() + "&concernedUserAccount=" + recipe.getAuthorAccount() + "&isConcerned=" + isConcerned;
                String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "updateUserConcern", param);
                if (result == null) {
                    InternetTooltip.tooltip(RecipeDetailActivity.this, HttpClientUtils.HTTP_REQUEST_ERROR);
                } else if (result.equals("false")) {
                    InternetTooltip.tooltip(RecipeDetailActivity.this, "操作失败！");
                } else {
                    InternetTooltip.tooltip(RecipeDetailActivity.this, "操作成功！");
                    //更改关注按钮状态
                    isConcerned = !isConcerned;
                    changeState(isConcerned, "1已关注", "1关注", R.drawable.menu_channel_hot, "关注", (int) concernButton.getTextSize());
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
        String[] accessory = accessories.split("##");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        List<TextView> accessoryText = new ArrayList<>();
        for (int i = 0; i < accessory.length; i++) {
            TextView textView = new TextView(RecipeDetailActivity.this);
            textView.setText(accessory[i].replace('&', ' ').toString());
            textView.setLayoutParams(layoutParams);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            if (kind.equals("main"))
                recipeMainIngredient.addView(textView);
            else
                recipeAccessories.addView(textView);

        }
    }

    private boolean collectRecipe(Long account, String recipeNum, String postMethod) {
        String param = "userAccount=" + account + "&recipeNum=" + recipeNum;
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "/" + postMethod, param);
        if (result == null) {
            InternetTooltip.tooltip(RecipeDetailActivity.this, "请求出现异常，请检查是否网络未连接");
            return false;
        }
        return result.equals("true") ? true : false;
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
    private void changeState(boolean state, String isMessage, String notMessage, int image, String button, int textSize) {

        String concernMessage = state ? isMessage : notMessage;
        SpannableString spannableString = new SpannableString(concernMessage);
        Drawable drawable = getResources().getDrawable(image);
        drawable.setBounds(0, 0, textSize, textSize);
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (state) {
            if (button.equals("收藏")) {
                collectButton.setText(spannableString);
                collectButton.setTextColor(getResources().getColor(R.color.colorRed));
            } else {
                concernButton.setText(spannableString);
                concernButton.setTextColor(getResources().getColor(R.color.colorRed));
            }
        } else {
            if (button.equals("收藏")) {
                collectButton.setText(spannableString);
                collectButton.setTextColor(getResources().getColor(R.color.colorBlack));
            } else {
                concernButton.setText(spannableString);
                concernButton.setTextColor(getResources().getColor(R.color.colorBlack));
            }

        }

    }

}

