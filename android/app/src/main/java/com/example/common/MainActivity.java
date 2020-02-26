package com.example.common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classify.ClassifyFragment;
import com.example.R;
import com.example.homepage.HomePageFragment;
import com.example.hot.HotFragment;
import com.example.mime.MimeLoginedFragment;
import com.example.mime.MimeNotLoginFragment;
import com.example.mime.entity.UserInfo;
import com.example.mime.entity.UserManager;
import com.example.nutritionQuery.NutritionFragment;


/**
 * Created by Coder-pig on 2015/8/28 0028.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //UI Object
    //  private TextView top_text;
    private TextView homePageText;
    private TextView classifyText;
    private TextView hotText;
    private TextView nutritionQueryText;
    private TextView mimeText;
    private FrameLayout ly_content;
    private Context context;

    //Fragment Object
    public static Fragment fg1, fg2, fg3, fg4, fg5, fg6;

    private FragmentManager fManager;
    private FragmentTransaction fTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fManager = getFragmentManager();
        bindViews();
        homePageText.performClick();   //模拟一次点击，既进去后选择第一项
    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        // top_text = findViewById(R.id.top_text);
        homePageText = findViewById(R.id.home_page_text);
        classifyText = findViewById(R.id.classify_text);
        hotText = findViewById(R.id.hot_text);
        nutritionQueryText = findViewById(R.id.nutrition_query_text);
        mimeText = findViewById(R.id.mime_text);
        ly_content = findViewById(R.id.ly_content);
        homePageText.setOnClickListener(this);
        classifyText.setOnClickListener(this);
        hotText.setOnClickListener(this);
        nutritionQueryText.setOnClickListener(this);
        mimeText.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected() {
        homePageText.setSelected(false);
        classifyText.setSelected(false);
        hotText.setSelected(false);
        nutritionQueryText.setSelected(false);
        mimeText.setSelected(false);
    }

    //隐藏所有Fragment
    public static void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) fragmentTransaction.hide(fg1);
        if (fg2 != null) fragmentTransaction.hide(fg2);
        if (fg3 != null) fragmentTransaction.hide(fg3);
        if (fg4 != null) fragmentTransaction.hide(fg4);
        if (fg5 != null) fragmentTransaction.hide(fg5);
        if (fg6 != null) fragmentTransaction.hide(fg6);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()) {
            case R.id.home_page_text:
                setSelected();
                homePageText.setSelected(true);
                if (fg1 == null) {
                    fg1 = new HomePageFragment();
                    fTransaction.add(R.id.ly_content, fg1);
                }
                fTransaction.addToBackStack(null);
                fTransaction.show(fg1);
                break;
            case R.id.classify_text:
                setSelected();
                classifyText.setSelected(true);
                if (fg2 == null) {
                    fg2 = new ClassifyFragment();
                    fTransaction.add(R.id.ly_content, fg2);
                }
                fTransaction.addToBackStack(null);
                fTransaction.show(fg2);
                break;
            case R.id.hot_text:
                setSelected();
                hotText.setSelected(true);
                if (fg3 == null) {
                    fg3 = new HotFragment();
                    fTransaction.add(R.id.ly_content, fg3);
                }
                fTransaction.addToBackStack(null);
                fTransaction.show(fg3);
                break;
            case R.id.nutrition_query_text:
                setSelected();
                nutritionQueryText.setSelected(true);
                if (fg4 == null) {
                    fg4 = new NutritionFragment();
                    fTransaction.add(R.id.ly_content, fg4);
                }
                fTransaction.addToBackStack(null);
                fTransaction.show(fg4);
                break;
            case R.id.mime_text:
                setSelected();
                mimeText.setSelected(true);
                boolean hasUserInfo = UserManager.getInstance().hasUserInfo(getApplicationContext());
                if (!hasUserInfo) {
                    if (fg5 == null) {
                        fg5 = new MimeNotLoginFragment();
                        fTransaction.add(R.id.ly_content, fg5);
                    }
                    fTransaction.addToBackStack(null);
                    fTransaction.show(fg5);
                } else {
                    if (fg6 == null) {
                        fg6 = new MimeLoginedFragment();
                        fTransaction.add(R.id.ly_content, fg6);
                    }
                    fTransaction.addToBackStack(null);
                    fTransaction.show(fg6);
                }
                break;
        }
        fTransaction.commit();
    }
}
