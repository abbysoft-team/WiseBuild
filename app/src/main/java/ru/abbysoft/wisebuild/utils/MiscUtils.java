package ru.abbysoft.wisebuild.utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.VKApiManager;
import com.vk.api.sdk.exceptions.VKApiException;
import com.vk.api.sdk.exceptions.VKApiExecutionException;
import com.vk.api.sdk.internal.ApiCommand;
import com.vk.api.sdk.requests.VKRequest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.api.vk.AccountInfoRequest;
import ru.abbysoft.wisebuild.model.VkUser;

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

    /**
     * Get logged user name
     *
     * Method access network api so it is async
     * provide callback to get result of operation
     *
     * @param callback callback for request
     */
    public static void getUserNameAsync(VKApiCallback<VkUser> callback) {
        VK.execute(new AccountInfoRequest(), callback);
    }
}
