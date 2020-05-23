package com.example.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.example.R;
import com.example.common.RecipeDetailActivity;

public class InternetTooltip {

    private static AlertDialog alertDialog;
    public static final String DATA_ERROR = "数据出现异常！";
    private static Context con;

    public static void tip(Context context, String message) {
        con = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.tip_item, null);
        TextView tipDialogText = view.findViewById(R.id.tip_item_text);
        alertDialog = dialogBuilder.create();
        tipDialogText.setText(message);
        alertDialog.setView(view);
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TimeCount timeCount = new TimeCount(1000, 10000);
        timeCount.start();
    }

    private static class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            alertDialog.dismiss();
        }
    }
}
