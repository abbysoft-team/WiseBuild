package ru.abbysoft.wisebuild.utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import ru.abbysoft.wisebuild.R;

/**
 * Misc helper functions
 *
 * @author apopov
 */
public class MiscUtils {

    /**
     * Show error alert dialog and finish activity
     *
     * @param title title of dialog
     * @param message message of dialog
     * @param activity activity which should create dialog
     */
    public static void showErrorDialogAndFinish(String title, String message, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> activity.finish());
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
