package ru.abbysoft.wisebuild.validation;

import android.content.Context;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.QuickRule;

public class NotEmptyQuickRule extends QuickRule<TextView> {
    @Override
    public boolean isValid(TextView view) {
        if (view.getText().toString().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public String getMessage(Context context) {
        return "This field required";
    }
}
