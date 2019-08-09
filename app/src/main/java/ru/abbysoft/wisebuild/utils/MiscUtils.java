package ru.abbysoft.wisebuild.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.VKApiManager;
import com.vk.api.sdk.exceptions.VKApiException;
import com.vk.api.sdk.exceptions.VKApiExecutionException;
import com.vk.api.sdk.internal.ApiCommand;
import com.vk.api.sdk.requests.VKRequest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import ru.abbysoft.wisebuild.R;
import ru.abbysoft.wisebuild.api.vk.AccountInfoRequest;
import ru.abbysoft.wisebuild.assembly.CreateAssemblyActivity;
import ru.abbysoft.wisebuild.model.ComputerPart;
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
        showDialogAndFinish(title, message, activity, android.R.drawable.ic_dialog_alert);
    }

    /**
     * Show message dialog and finish activity
     *
     * @param title title of dialog
     * @param message message of dialog
     * @param activity activity which should create dialog
     */
    public static void showMessageDialogAndFinish(String title, String message, Activity activity) {
        showDialogAndFinish(title, message, activity, android.R.drawable.ic_dialog_info);
    }

    /**
     * Show dialog with given message title and icon
     *
     * @param title title of dialog
     * @param message message of dialog
     * @param activity activity which should create dialog
     * @param iconId icon id for dialog
     */
    public static void showDialogAndFinish(String title, String message, Activity activity, int iconId) {
        showDialog(title, message, activity, iconId, true);
    }

    /**
     * Show error dialog
     *
     * @param error error message
     */
    public static void showErrorDialog(String error, Activity activity) {
        showDialog("Error", error, activity, android.R.drawable.ic_dialog_alert, false);
    }

    private static void showDialog(String title, String message, Activity activity,
                                   int iconId, boolean finish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            if (finish) activity.finish();
        });
        builder.setIcon(iconId);

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

    /**
     * Get price int from field that contains currency
     * @param priceField some field that contains currency value
     * @return value in int
     */
    public static Integer getPriceFromCurrencyField(@NonNull TextView priceField) {
        String text = priceField.getText().toString();

        text = text.replaceAll("\\D", "");
        text = text.trim();

        return Integer.parseInt(text);
    }

    /**
     * Get available values of given enum through reflection api
     * @param enumClass enum class
     * @return available values of given enum
     * @throws NoSuchMethodException cannot get values of enum
     * @throws InvocationTargetException cannot get values of enum
     * @throws IllegalAccessException cannot get values of enum
     */
    public static Enum<?>[] getEnumValues(Class<?> enumClass) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        return (Enum<?>[]) enumClass.getMethod("values").invoke(null);
    }

    public static ComputerPart instantiatePartOfType(ComputerPart.ComputerPartType partType)
            throws InstantiationException, IllegalAccessException {
        return (ComputerPart) partType.getObjectClass().newInstance();
    }

    @NotNull
    public static String getGetterName(@NotNull String fieldName, Class<?> claz) {
        if (claz == boolean.class) {
            return "is" + capitalizeFirstChar(fieldName);
        } else {
            return "get" + capitalizeFirstChar(fieldName);
        }
    }

    @NotNull
    public static String getSetterName(@NotNull String fieldName) {
        return "set" + capitalizeFirstChar(fieldName);
    }

    public static String capitalizeFirstChar(@NotNull String string) {
        String firstLetter = "" + string.charAt(0);
        return firstLetter.toUpperCase() + string.substring(1);
    }

    /**
     * Configure specified field as price field
     *
     * to the left of field value will be added currency sign
     *
     * @param editText field
     */
    public static void configureAsPriceField(@NotNull EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editText.getText().toString();

                if (text.isEmpty()) {
                    return;
                }
                if (text.startsWith("$")) {
                    return;
                }
                if (text.contains("$")) {
                    text = text.replace("$", "");
                }

                text = "$" + text;
                editText.setText(text.trim());
                editText.setSelection(text.length());
            }
        });
    }

    /**
     * Convert string to another class
     * @param string
     * @param claz
     * @return
     */
    public static Object convertString(@NotNull String string, @NotNull Class<?> claz) {
        if (int.class.isAssignableFrom(claz)) {
            return Integer.parseInt(string);
        }
        if (long.class.isAssignableFrom(claz)) {
            return Long.parseLong(string);
        }
        if (float.class.isAssignableFrom(claz)) {
            return Float.parseFloat(string);
        }
        if (double.class.isAssignableFrom(claz)) {
            return Double.parseDouble(string);
        }
        if (String.class.isAssignableFrom(claz)) {
            return string;
        }

        return new UnsupportedOperationException("Cannot convert string to " + claz.getName());
    }
}
