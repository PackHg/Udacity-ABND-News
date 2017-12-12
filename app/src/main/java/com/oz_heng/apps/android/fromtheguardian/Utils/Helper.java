package com.oz_heng.apps.android.fromtheguardian.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Helper methods.
 */
public final class Helper {

    private Helper() {}

    /**
     * Show a {@link Toast} with the text parameter.
     * @param context The activity context.
     * @param text Text to be displayed.
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show {@link Snackbar} with the text parameter.
     * @param view Contextual view.
     * @param text Text to be displayed.
     */
    public static void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
