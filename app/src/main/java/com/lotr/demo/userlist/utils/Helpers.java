package com.lotr.demo.userlist.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Contains any auxiliary methods
 */
public class Helpers {

    public static void showEasyDialog(Context context, int resId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(resId));
        builder.setPositiveButton("OK", listener);
        builder.show();
    }
}
