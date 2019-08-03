package ru.abbysoft.wisebuild.databinding;

import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;

/**
 * Some parameter of a part
 *
 * @author apopov
 */
public class FieldMapping {

    private final String name;
    private final Object value;
    private final Class valueClass;
    private int inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    private FieldType fieldType = FieldType.NULL;


    /**
     * Default constructor
     *
     * @param name
     * @param value
     * @param valueClass
     */
    public FieldMapping(String name, Object value, @NonNull Class valueClass) {
        this.name = name;
        this.value = value;
        this.valueClass = valueClass;

        setInputType(valueClass);
    }

    private void setInputType(Class valueClass) {
        if (valueClass == int.class || valueClass == long.class
                || valueClass == byte.class || valueClass == short.class) {
            inputType = InputType.TYPE_CLASS_NUMBER;
        } else if (valueClass == double.class || valueClass == float.class) {
            inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public int getInputType() {
        return inputType;
    }

    /**
     * Set field input type
     * @param inputType input type from InputType
     */
    public FieldMapping setInputType(int inputType) {
        this.inputType = inputType;

        return this;
    }

    /**
     * Get class of value
     * @return class
     */
    public Class getValueClass() {
        return valueClass;
    }

    public String getValueAsString() {
        return value == null ? "" : value.toString();
    }

    /**
     * Apply all field parameters
     * @param editText field
     * @return field with all parameters
     */
    public EditText applyFormat(EditText editText) {
        editText.setInputType(inputType);

        return editText;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public FieldMapping setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;

        return this;
    }

    public enum FieldType {
        NULL,
        PRICE
    }
}
