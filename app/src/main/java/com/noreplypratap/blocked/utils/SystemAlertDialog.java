package com.noreplypratap.blocked.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class SystemAlertDialog {
    public static void showBlockedKeywordDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Blocked Keyword")
                .setMessage("The keyword 'abcde' is blocked.")
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
