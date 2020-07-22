package com.example.mime;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.R;
import com.example.common.MainActivity;
import com.example.mime.activity.MimeLoginActivity;
import com.example.mime.entity.UserManager;


public class MimeNotLoginFragment extends Fragment {
    private TextView userName;
    private TextView collectView;
    private TextView myRecipesView;
    private TextView makeRecipeView;
    private TextView logOut;
    private TextView fansCount;
    private TextView attentionsCount;
    private TextView intro;
    private static final int GO_LOGIN = 1;
    private static final String PAGE_COUNT = "pageCount";
    private static final int FIRST_PAGE = 1;
    private final int resource = R.layout.mime_not_login_fragment;

    private LayoutInflater inflater;
    private ViewGroup container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        boolean hasInfo = UserManager.getInstance().hasUserInfo(getActivity());
        View view = inflater.inflate(R.layout.mime_not_login_fragment, container, false);
        Button loginButton = view.findViewById(R.id.mime_not_login_fragment_login_button);//获取登录按钮对象
        //登录按钮对象添加点击监听器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MimeLoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.mime.action.login");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("data");
                if ("refresh".equals(msg)) {
                    System.out.println("更新后本地是否存在缓存" + UserManager.getInstance().hasUserInfo(getActivity()));
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    MainActivity.hideAllFragment(fragmentTransaction);
                    if (MainActivity.fg6 == null) {
                        MainActivity.fg6 = new MimeLoginedFragment();
                        fragmentTransaction.add(R.id.ly_content, MainActivity.fg6);
                    }
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.show(MainActivity.fg6);
                    //   fragmentTransaction.show(MainActivity.fg6);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }


}
