package com.lotr.demo.userlist.utils;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Contains any auxiliary methods
 */
public class Helpers {

    public static void showEasyDialog(Context context, String title, int resId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(context.getString(resId));
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
