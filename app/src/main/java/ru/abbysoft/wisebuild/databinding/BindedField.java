package ru.abbysoft.wisebuild.databinding;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * Field that have binded model to it
 *
 * @author apopov
 */
public class BindedField extends AppCompatEditText {

    private final FieldMapping mapping;

    /**
     * Default constructor
     *
     * @param context context
     */
    public BindedField(Context context, FieldMapping mapping) {
        super(context);

        this.mapping = mapping;

        configureField();
    }

    private void configureField() {
        mapping.applyFormat(this);
    }
}
