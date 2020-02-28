package com.example.utils;

import android.app.AlertDialog;
import android.content.Context;

public class InternetTooltip {
    public static void tooltip(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("是", null);
        builder.show();
    }
}
