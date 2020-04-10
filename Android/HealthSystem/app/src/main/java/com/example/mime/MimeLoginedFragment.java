package com.example.mime;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.R;
import com.example.common.SpacesItemDecoration;
import com.example.entity.UserInfoClient;
import com.example.mime.activity.MimeCollectActivity;
import com.example.mime.activity.MimeConcernedAndFansActivity;
import com.example.mime.activity.MimeHealthDataSearchActivity;
import com.example.mime.activity.MimeInformationActivity;
import com.example.mime.activity.MimeLoginActivity;
import com.example.mime.activity.MimeMakeRecipeActivity;
import com.example.mime.activity.MimeMyRecipesActivity;
import com.example.mime.entity.UserManager;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class MimeLoginedFragment extends Fragment {

    private SimpleDraweeView userImage;
    private TextView userName;
    private TextView searchNutritionView;
    private TextView collectView;
    private TextView myRecipesView;
    private TextView makeRecipeView;
    private TextView logOut;
    private TextView fansCount;
    private TextView concernedCount;
    private TextView intro;
    private static final String PAGE_COUNT = "pageCount";
    private static final int FIRST_PAGE = 1;

    private LinearLayout tagLayout;
    private RecyclerView tagsRecyclerView;
    private static TagAdapter tagAdapter;
    private static Context context;
    private Bitmap picBitmap;
    private AlertDialog dialog;
    private UserInfoClient userInfo = new UserInfoClient();
    private LocalBroadcastManager broadcastManager;
    private BroadCastReceiver broadCastReceiver;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.mime_fregment, container, false);
        context = getActivity();
        userName = view.findViewById(R.id.mime_user_name);
        String param = "userAccount=" + UserManager.getInstance().getUserInfo(getActivity()).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getUserInfoClient", param);

        if (result == null) {
            InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
        } else {
            JSONObject object = JSONObject.parseObject(result);
            userInfo = JSONObject.toJavaObject(object, UserInfoClient.class);
        }
        userImage = view.findViewById(R.id.mime_author_image);
        Bitmap bitmap = PicUtils.byteConvertToBitmap(userInfo.getPic());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(new BitmapDrawable(bitmap))
                .build();
        RoundingParams params = RoundingParams.fromCornersRadius(5f);
        params.setRoundAsCircle(true);
        hierarchy.setRoundingParams(params);
        userImage.setHierarchy(hierarchy);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });


        userName.setText(userInfo.getUserName());
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeInformationActivity.class);
                startActivity(intent);
            }
        });
        // tagLayout = view.findViewById(R.id.mime_tags_layout);
        tagsRecyclerView = view.findViewById(R.id.mime_tags_recycler_view);
        tagsRecyclerView.addItemDecoration(new SpacesItemDecoration());
        initTags();
        concernedCount = view.findViewById(R.id.mime_attention_count);//获取关注数量按钮对象
        concernedCount.setText(String.valueOf(userInfo.getAttentions()));
        view.findViewById(R.id.mime_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeConcernedAndFansActivity.class);
                intent.putExtra("activityKind", "concern");
                startActivity(intent);
            }
        });

        fansCount = view.findViewById(R.id.mime_fan_count);//获取粉丝数量对象
        fansCount.setText(String.valueOf(userInfo.getFans()));
        view.findViewById(R.id.mime_fan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeConcernedAndFansActivity.class);
                intent.putExtra("activityKind", "fan");
                startActivity(intent);
            }
        });
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadCastReceiver = new BroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ConcernAction");
        broadcastManager.registerReceiver(broadCastReceiver, intentFilter);

        intro = view.findViewById(R.id.mime_intro);
        intro.setText(userInfo.getIntro());
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIntro();
            }
        });

        searchNutritionView = view.findViewById(R.id.mime_search_nutrition);
        searchNutritionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeHealthDataSearchActivity.class);
                intent.putExtra("searchKind", "2");
                startActivity(intent);
            }
        });
        collectView = view.findViewById(R.id.mime_collect_recipes);
        collectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeCollectActivity.class);
                intent.putExtra(PAGE_COUNT, FIRST_PAGE);
                startActivity(intent);
            }
        });
        myRecipesView = view.findViewById(R.id.mime_my_recipes);
        myRecipesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeMyRecipesActivity.class);
                intent.putExtra(PAGE_COUNT, FIRST_PAGE);
                startActivity(intent);
            }
        });
        makeRecipeView = view.findViewById(R.id.mime_make_recipe);
        makeRecipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeMakeRecipeActivity.class);
                intent.putExtra(PAGE_COUNT, FIRST_PAGE);
                startActivity(intent);
            }
        });
        logOut = view.findViewById(R.id.mime_log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用用户管理对象进行用户的退出登录操作
                UserManager.getInstance().logOut(getActivity());
                //使用Intent跳转到MimeLoginActivity进入登录注册页面
                Intent intent = new Intent(getActivity(), MimeLoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void changeIntro() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.mime_dialog_view, null);
        LinearLayout linearLayout = view.findViewById(R.id.mime_dialog_layout);
        final EditText introText = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        introText.setMinHeight(300);
        introText.setBackground(null);
        introText.setHint(intro.getText().toString());
        introText.setLayoutParams(layoutParams);

        LinearLayout buttonLayout = new LinearLayout(getActivity());
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        Button submitButton = new Button(getActivity());
        Button cancelButton = new Button(getActivity());
        submitButton.setText("提交");
        submitButton.setWidth(getResources().getDisplayMetrics().widthPixels / 2);
        cancelButton.setText("取消");
        cancelButton.setWidth(getResources().getDisplayMetrics().widthPixels / 2);
        buttonLayout.addView(submitButton);
        buttonLayout.addView(cancelButton);

        linearLayout.addView(introText);
        linearLayout.addView(buttonLayout);

        dialog = dialogBuilder.create();
        dialog.setView(view);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intro = introText.getText().toString();
                uploadIntro(intro);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initTags() {
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        tagsRecyclerView.setLayoutManager(linearLayoutManager);
        List<String> values = new ArrayList<>();
        String param = "userAccount=" + UserManager.getInstance().getUserInfo(getActivity()).getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getUserTags", param);
        if (result == null) {
            InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
        } else {
            List<String> re = JSONArray.parseArray(result, String.class);
            //  System.out.println("集合的数量为：" + re.size());
            if (re.size() > 0) values.addAll(re);
            values.add("添加");
            tagAdapter = new TagAdapter(getActivity(), values);
            tagsRecyclerView.setAdapter(tagAdapter);
        }
    }

    public static void refresh(Set<String> value) {
        List<String> values = tagAdapter.getValues();
        values.clear();
        values.addAll(value);
        values.add("添加");
        tagAdapter.setValues(values);
        tagAdapter.notifyDataSetChanged();
    }


    private void changeImage() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.mime_dialog_view, null);
        LinearLayout linearLayout = view.findViewById(R.id.mime_dialog_layout);
        Button localPicButton = new Button(context);
        Button takePicButton = new Button(context);
        Button cancelButton = new Button(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        localPicButton.setText("本地照片");
        takePicButton.setText("拍照");
        cancelButton.setText("取消");
        localPicButton.setLayoutParams(layoutParams);
        cancelButton.setLayoutParams(layoutParams);
        linearLayout.addView(localPicButton);
        linearLayout.addView(takePicButton);
        linearLayout.addView(cancelButton);
        dialog = dialogBuilder.create();
        dialog.setView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        localPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //int num = picKind == RECIPE_IMAGE_KIND ? TAKE_PIC_FOR_RECIPE_IMAGE_CODE : TAKE_PIC_FOR_MEASURE_IMAGE_CODE;
                startActivityForResult(intent, 2);
            }
        });
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // int num = picKind == RECIPE_IMAGE_KIND ? TAKE_PIC_FOR_RECIPE_IMAGE_CODE : TAKE_PIC_FOR_MEASURE_IMAGE_CODE;
                startActivityForResult(intent, 1);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        switch (requestCode) {
            //选择拍照
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    picBitmap = (Bitmap) bundle.get("data");
                    System.out.println("食谱图片已获取到拍照后的图片");

                }
                break;
            //选择图片库的图片
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    picBitmap = PicUtils.getBitmapFromUri(uri, getActivity());
                }
                break;
        }
        uploadPic(picBitmap);
    }

    private void uploadPic(Bitmap picBitmap) {
        Map<String, String> params = new HashMap<>();
        params.put("userAccount", UserManager.getInstance().getUserInfo(getActivity()).getAccount().toString());
        List<Bitmap> images = new ArrayList<>();
        images.add(picBitmap);
        String result = HttpClientUtils.uploadImage(images, HttpClientUtils.serverUrl + "uploadUserImage"
                , params, "");
        System.out.println("返回的结果为:" + result);
        if (result == null || result.equals("false")) {
            InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示");
            builder.setMessage("提交成功！");
            builder.show();
            GenericDraweeHierarchyBuilder builder1 =
                    new GenericDraweeHierarchyBuilder(getResources());
            GenericDraweeHierarchy hierarchy1 = builder1
                    .setPlaceholderImage(new BitmapDrawable(picBitmap))
                    .build();
            RoundingParams params1 = RoundingParams.fromCornersRadius(5f);
            params1.setRoundAsCircle(true);
            hierarchy1.setRoundingParams(params1);
            userImage.setHierarchy(hierarchy1);
        }

    }

    private void uploadIntro(String introText) {
        String param = "intro=" + introText + "&userAccount=" + userInfo.getAccount();
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "uploadIntro", param);
        if (result == null || result.equals("false")) {
            InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示");
            builder.setMessage("提交成功！");
            builder.show();
            //userImage.setImageBitmap(picBitmap);
            intro.setText(introText);
        }
        dialog.dismiss();
    }

    private class BroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ConcernAction")) {
                boolean concern = (boolean) intent.getExtras().get("concern");
                if (concern)
                    concernedCount.setText(String.valueOf(Integer.valueOf(concernedCount.getText().toString()) + 1));
                else
                    concernedCount.setText(String.valueOf(Integer.valueOf(concernedCount.getText().toString()) - 1));
            }
        }
    }
}
