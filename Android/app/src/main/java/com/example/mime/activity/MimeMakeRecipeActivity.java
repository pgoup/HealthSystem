package com.example.mime.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.entity.RecipeClient;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class MimeMakeRecipeActivity extends AppCompatActivity {
    private ImageView recipeImage;
    private EditText recipeNameEdit;
    private EditText mainIngredient;
    private EditText mainIngredientAmount;
    private EditText accessory;
    private EditText accessoryAmount;
    private EditText firstMeasureIntro;
    private ImageView firstMeasureImage;
    private Spinner recipeKindSpinner;
    private List<Spinner> recipeKindSpinners = new ArrayList<>();
    private Set<String> recipeKinds = new HashSet<>();
    private LinearLayout addRecipeKindLayout;
    private List<LinearLayout> recipeKindLayouts = new ArrayList<>();
    //主食料相关控件
    private LinearLayout addMainIngredientLayout;
    private List<EditText> mainIngredientEditTexts = new ArrayList<>();
    private List<EditText> mainIngredientAmountEditTexts = new ArrayList<>();
    private List<LinearLayout> mainIngredientLayouts = new ArrayList<>();
    //调料相关控件
    private LinearLayout addAccessoryLayout;
    private List<EditText> accessoryEditTexts = new ArrayList<>();
    private List<EditText> accessoryAmountEditTexts = new ArrayList<>();
    private List<LinearLayout> accessoryLayouts = new ArrayList<>();
    //方法相关控件
    private LinearLayout addMeasureLayout;
    private List<EditText> measureIntroEditTexts = new ArrayList<>();
    private List<ImageView> measureImageViews = new ArrayList<>();
    private List<LinearLayout> measureLayouts = new ArrayList<>();
    private List<TextView> measureNumTextViews = new ArrayList<>();
    private List<ImageView> imageViews = new ArrayList<>();
    private static final String COULD_NOT_DELETE_MESSAGE = "至少保留一项，无法继续删除！";
    private static final String MAIN_INGREDIENT_TIP_MESSAGE = "食材：如鸡蛋";
    private static final String MAIN_INGREDIENT_AMOUNT_TIP_MESSAGE = "用量：如一只";
    private static final String ACCESSORY_TIP_MESSAGE = "调料：如酱油";
    private static final String ACCESSORY_AMOUNT_TIP_MESSAGE = "用量：如10克";
    //为食谱图片选择拍照
    private static final int TAKE_PIC_FOR_RECIPE_IMAGE_CODE = 1;
    //为食谱图片选择本地照片
    private static final int IMAGE_RESULT_FOR_RECIPE_IMAGE_CODE = 2;
    //制作方法选择拍照照片
    private static final int TAKE_PIC_FOR_MEASURE_IMAGE_CODE = 3;
    //制作方法选择本地照片
    private static final int IMAGE_RESULT_FOR_MEASURE_IMAGE_CODE = 4;
    private static final int RECIPE_IMAGE_KIND = 1;
    private static final int MEASURE_IMAGE_KIND = 2;
    private AlertDialog dialog;
    private int screenWidth;
    private Bitmap picBitmap;
    private Map<String, List<String>> allRecipeKinds = new LinkedHashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mime_make_recipe);
        getRecipeKinds();
        initView();
        findViewById(R.id.make_recipe_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submitButton = findViewById(R.id.mime_create_recipe_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadRecipe();
            }
        });
    }
    private void uploadRecipe() {
        String recipeName = recipeNameEdit.getText().toString();
        String kind = "";
        byte[] pic = getImageByteArray(recipeImage);
        String mainIngredient = getIngredient(mainIngredientEditTexts, mainIngredientAmountEditTexts);
        String accessory = getIngredient(accessoryEditTexts, accessoryAmountEditTexts);
        String author = UserManager.getInstance().getUserInfo(this).getUserName();
        RecipeClient recipeClient = new RecipeClient(0, kind, recipeName, pic, mainIngredient, accessory, null, 0, 0, author);
        postRequest(recipeClient, getMeasures(measureIntroEditTexts));
    }
    private void postRequest(RecipeClient recipeClient, List<String> measures) {
        Map<String, String> param = new HashMap<>();
        param.put("recipeName", recipeClient.getRecipeName());
        param.put("recipeKind", getKind());
        param.put("mainIngredient", recipeClient.getMainIngredient());
        param.put("accessory", recipeClient.getAccessories());
        param.put("measure", JSONObject.toJSONString(measures));
        param.put("author", recipeClient.getAuthor());
        param.put("userAccount", UserManager.getInstance().getUserInfo(MimeMakeRecipeActivity.this).getAccount());
        imageViews.addAll(measureImageViews);
        imageViews.add(recipeImage);
        List<Bitmap> bitmaps = new ArrayList<>();
        for (ImageView imageView : imageViews) {
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache(true);
            bitmaps.add(imageView.getDrawingCache());
        }
        param.put("imageCount", String.valueOf(bitmaps.size()));
        String result = HttpClientUtils.uploadImage(bitmaps, HttpClientUtils.serverUrl + "uploadRecipe"
                , param, "");
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("提交成功！");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }
        imageViews.clear();
        return;
    }
    private List<String> getMeasures(List<EditText> measureIntroEditTexts) {
        List<String> measureItems = new ArrayList<>();
        for (EditText editText : measureIntroEditTexts)
            measureItems.add(editText.getText().toString());
        return measureItems;
    }
    private String getKind() {
        for (Spinner spinner : recipeKindSpinners)
            recipeKinds.add(spinner.getSelectedItem().toString());
        StringBuilder recipeKind = new StringBuilder();
        Iterator iterator = recipeKinds.iterator();
        while (iterator.hasNext()) {
            recipeKind.append(iterator.next());
            recipeKind.append("&&");
        }
        return recipeKind.toString();
    }
    private byte[] getImageByteArray(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);
        Bitmap bitmap = imageView.getDrawingCache();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    private String getIngredient(List<EditText> intros, List<EditText> amounts) {
        StringBuilder ingredient = new StringBuilder();
        for (int i = 0; i < intros.size(); i++) {
            ingredient.append(intros.get(i).getText().toString());
            ingredient.append("&&");
            ingredient.append(amounts.get(i).getText().toString());
            ingredient.append("##");
        }
        return ingredient.toString();
    }
    private void initView() {
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        initRecipeNameAndImage();
        initRecipeKind();
        initMainIngredient();
        initAccessory();
        initMeasure();
    }
    private void initRecipeNameAndImage() {
        recipeNameEdit = findViewById(R.id.mime_create_recipe_name);
        recipeImage = findViewById(R.id.mime_create_recipe_image);
        recipeImage.setAdjustViewBounds(true);
        recipeImage.setMaxHeight(screenWidth);
        recipeImage.setMaxWidth(screenWidth);
        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPic(RECIPE_IMAGE_KIND);
            }
        });
    }
    private void initRecipeKind() {
        final Spinner firstSpinner = findViewById(R.id.mime_recipe_kind_first_spinner);
        final Spinner secondSpinner = findViewById(R.id.mime_recipe_kind_second_spinner);
        Iterator iterator = allRecipeKinds.keySet().iterator();
        List<String> firstValues = new ArrayList<>();
        while (iterator.hasNext())
            firstValues.add(iterator.next().toString());
        firstSpinner.setDropDownHorizontalOffset(1000);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, firstValues);
        firstSpinner.setAdapter(adapter1);
        firstSpinner.setDropDownHorizontalOffset(1000);
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String firstSpinnerText = firstSpinner.getSelectedItem().toString();
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MimeMakeRecipeActivity.this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, allRecipeKinds.get(firstSpinnerText));
                secondSpinner.setAdapter(adapter2);
                secondSpinner.setDropDownHorizontalOffset(1000);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        recipeKindSpinners.add(secondSpinner);
        addRecipeKindLayout = findViewById(R.id.mime_create_add_recipe_kind_layout);
        Button addButton = findViewById(R.id.mime_create_add_recipe_kind_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipeKind();
            }
        });
        Button deleteButton = findViewById(R.id.mime_create_delete_recipe_kind_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeKindSpinners.size() > 1) {
                    addRecipeKindLayout.removeView(recipeKindLayouts.get(recipeKindLayouts.size() - 1));
                    recipeKindSpinners.remove(recipeKindSpinners.size() - 1);
                    recipeKindLayouts.remove(recipeKindLayouts.size() - 1);
                } else {
                    InternetTooltip.tip(MimeMakeRecipeActivity.this, COULD_NOT_DELETE_MESSAGE);
                }
            }
        });
    }
    private void getPic(final int picKind) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MimeMakeRecipeActivity.this);
        View view = View.inflate(MimeMakeRecipeActivity.this, R.layout.mime_dialog_view, null);
        LinearLayout linearLayout = view.findViewById(R.id.mime_dialog_layout);
        Button takePicButton = new Button(this);
        Button picButton = new Button(this);
        Button cancelButton = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        takePicButton.setText("拍照");
        picButton.setText("本地照片");
        cancelButton.setText("取消");
        linearLayout.addView(takePicButton);
        linearLayout.addView(picButton);
        linearLayout.addView(cancelButton);
        dialog = dialogBuilder.create();
        dialog.setView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                int num = picKind == RECIPE_IMAGE_KIND ? TAKE_PIC_FOR_RECIPE_IMAGE_CODE : TAKE_PIC_FOR_MEASURE_IMAGE_CODE;
                startActivityForResult(intent, num);
            }
        });
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                int num = picKind == RECIPE_IMAGE_KIND ? IMAGE_RESULT_FOR_RECIPE_IMAGE_CODE : IMAGE_RESULT_FOR_MEASURE_IMAGE_CODE;
                startActivityForResult(intent, num);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        switch (requestCode) {
            //选择拍照
            case TAKE_PIC_FOR_RECIPE_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    picBitmap = (Bitmap) bundle.get("data");
                    recipeImage.setImageBitmap(picBitmap);
                }
                break;
            //选择图片库的图片
            case IMAGE_RESULT_FOR_RECIPE_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    picBitmap = PicUtils.getBitmapFromUri(uri, this);
                    recipeImage.setImageBitmap(picBitmap);
                }
                break;
            //为制作方法图片选择拍照照片
            case TAKE_PIC_FOR_MEASURE_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    picBitmap = (Bitmap) bundle.get("data");
                    measureImageViews.get(measureImageViews.size() - 1).setImageBitmap(picBitmap);
                }
                break;
            //为制作方法照片选择本地照片
            case IMAGE_RESULT_FOR_MEASURE_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    picBitmap = PicUtils.getBitmapFromUri(uri, this);
                    measureImageViews.get(measureImageViews.size() - 1).setImageBitmap(picBitmap);
                }
                break;
        }
    }
    private void initMainIngredient() {
        mainIngredient = findViewById(R.id.mime_create_main_ingredient_name);
        mainIngredient.setHint(MAIN_INGREDIENT_TIP_MESSAGE);
        mainIngredientEditTexts.add(mainIngredient);
        mainIngredientAmount = findViewById(R.id.mime_create_main_ingredient_amount);
        mainIngredientAmount.setHint(MAIN_INGREDIENT_AMOUNT_TIP_MESSAGE);
        mainIngredientAmountEditTexts.add(mainIngredientAmount);
        addMainIngredientLayout = findViewById(R.id.mime_create_add_mainIngredient_layout);
        Button addButton = findViewById(R.id.mime_create_add_main_ingredient_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient(MAIN_INGREDIENT_TIP_MESSAGE, MAIN_INGREDIENT_AMOUNT_TIP_MESSAGE, mainIngredientEditTexts, mainIngredientAmountEditTexts, mainIngredientLayouts, addMainIngredientLayout);
            }
        });
        Button deleteButton = findViewById(R.id.mime_create_delete_main_ingredient_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainIngredientEditTexts.size() > 0) {
                    addMainIngredientLayout.removeView(mainIngredientLayouts.get(mainIngredientLayouts.size() - 1));
                    mainIngredientEditTexts.remove(mainIngredientEditTexts.size() - 1);
                    mainIngredientAmountEditTexts.remove(mainIngredientAmountEditTexts.size() - 1);
                    mainIngredientLayouts.remove(mainIngredientLayouts.size() - 1);
                } else {
                    InternetTooltip.tip(MimeMakeRecipeActivity.this, COULD_NOT_DELETE_MESSAGE);
                }
            }
        });
    }
    private void initAccessory() {
        accessory = findViewById(R.id.mime_create_accessory_name);
        accessory.setHint(ACCESSORY_TIP_MESSAGE);
        accessoryEditTexts.add(accessory);
        accessoryAmount = findViewById(R.id.mime_create_accessory_amount);
        accessoryAmount.setHint(ACCESSORY_AMOUNT_TIP_MESSAGE);
        accessoryAmountEditTexts.add(accessoryAmount);
        addAccessoryLayout = findViewById(R.id.mime_create_add_accessory_layout);
        Button addButton = findViewById(R.id.mime_create_add_accessory_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient(ACCESSORY_TIP_MESSAGE, ACCESSORY_AMOUNT_TIP_MESSAGE, accessoryEditTexts, accessoryAmountEditTexts, accessoryLayouts, addAccessoryLayout);
            }
        });
        Button deleteButton = findViewById(R.id.mime_create_delete_accessory_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessoryEditTexts.size() > 0) {
                    addAccessoryLayout.removeView(accessoryLayouts.get(accessoryLayouts.size() - 1));
                    accessoryEditTexts.remove(accessoryEditTexts.size() - 1);
                    accessoryAmountEditTexts.remove(accessoryAmountEditTexts.size() - 1);
                    accessoryLayouts.remove(accessoryLayouts.size() - 1);
                } else {
                    InternetTooltip.tip(MimeMakeRecipeActivity.this, COULD_NOT_DELETE_MESSAGE);
                }
            }
        });
    }
    private void initMeasure() {
        firstMeasureIntro = findViewById(R.id.mime_create_measure_intro);
        firstMeasureImage = findViewById(R.id.mime_create_measure_image);
        measureIntroEditTexts.add(firstMeasureIntro);
        measureImageViews.add(firstMeasureImage);
        firstMeasureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPic(2);
            }
        });
        firstMeasureImage.setAdjustViewBounds(true);
        firstMeasureImage.setMaxHeight(screenWidth);
        firstMeasureImage.setMaxWidth(screenWidth);
        addMeasureLayout = findViewById(R.id.mime_create_add_measure_layout);
        Button addButton = findViewById(R.id.mime_create_add_measure_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeasure();
            }
        });
        Button deleteButton = findViewById(R.id.mime_create_delete_measure_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (measureIntroEditTexts.size() > 1) {
                    addMeasureLayout.removeView(measureLayouts.get(measureLayouts.size() - 1));
                    measureIntroEditTexts.remove(measureIntroEditTexts.size() - 1);
                    measureImageViews.remove(measureImageViews.size() - 1);
                    measureLayouts.remove(measureLayouts.size() - 1);
                } else {
                    InternetTooltip.tip(MimeMakeRecipeActivity.this, COULD_NOT_DELETE_MESSAGE);
                }
            }
        });
    }
    private void addRecipeKind() {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(layoutParams1);
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(layoutParams1);
        final Spinner firstSpinner = new Spinner(this);
        Iterator iterator = allRecipeKinds.keySet().iterator();
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
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MimeMakeRecipeActivity.this, R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, allRecipeKinds.get(firstSpinnerText));
                secondSpinner.setAdapter(adapter2);
                secondSpinner.setDropDownHorizontalOffset(1000);
                secondSpinner.setLayoutParams(layoutParams2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        recipeKindSpinners.add(secondSpinner);
        linearLayout1.addView(firstSpinner);
        linearLayout2.addView(secondSpinner);
        linearLayout.addView(linearLayout1);
        linearLayout.addView(linearLayout2);
        recipeKindLayouts.add(linearLayout);
        addRecipeKindLayout.addView(linearLayout);
    }
    private void addIngredient(String messageOne, String messageTwo, List<EditText> ingredientTexts, List<EditText> ingredientCountTexts, List<LinearLayout> layouts, LinearLayout addLayout) {
        LinearLayout linearLayout = new LinearLayout(MimeMakeRecipeActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        EditText ingredientText = new EditText(MimeMakeRecipeActivity.this);
        ingredientText.setHint(messageOne);
        ingredientText.setWidth(getResources().getDisplayMetrics().widthPixels / 2);
        ingredientTexts.add(ingredientText);
        linearLayout.addView(ingredientText);
        EditText accessoryText = new EditText(MimeMakeRecipeActivity.this);
        accessoryText.setHint(messageTwo);
        accessoryText.setWidth(getResources().getDisplayMetrics().widthPixels / 2);
        ingredientCountTexts.add(accessoryText);
        linearLayout.addView(accessoryText);
        layouts.add(linearLayout);
        addLayout.addView(linearLayout);
    }
    private void addMeasure() {
        LinearLayout linearLayout = new LinearLayout(MimeMakeRecipeActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        TextView numText = new TextView(MimeMakeRecipeActivity.this);
        numText.setText("步骤" + (measureNumTextViews.size() + 2));
        measureNumTextViews.add(numText);
        linearLayout.addView(numText);
        EditText measureIntroText = new EditText(MimeMakeRecipeActivity.this);
        measureIntroText.setHint("请输入步骤描述");
        measureIntroText.setWidth(getResources().getDisplayMetrics().widthPixels / 2);
        measureIntroEditTexts.add(measureIntroText);
        linearLayout.addView(measureIntroText);
        ImageView measureImage = new ImageView(MimeMakeRecipeActivity.this);
        measureImage.setAdjustViewBounds(true);
        measureImage.setMaxHeight(screenWidth);
        measureImage.setMaxWidth(screenWidth);
        measureImage.setImageResource(R.drawable.upload);
        measureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPic(MEASURE_IMAGE_KIND);
            }
        });
        measureImageViews.add(measureImage);
        linearLayout.addView(measureImage);
        measureLayouts.add(linearLayout);
        addMeasureLayout.addView(linearLayout);
    }
    private void getRecipeKinds() {
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getAllRecipeKind", "");
        if (result == null) {
            InternetTooltip.tip(this, HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        JSONObject object = JSONObject.parseObject(result);
        allRecipeKinds = JSONObject.toJavaObject(object, Map.class);
    }
}




