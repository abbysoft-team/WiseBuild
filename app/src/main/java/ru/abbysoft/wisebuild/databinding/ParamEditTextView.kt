package ru.abbysoft.wisebuild.databinding

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.mobsandgeeks.saripaar.Validator
import ru.abbysoft.wisebuild.utils.MiscUtils
import ru.abbysoft.wisebuild.validation.NotEmptyQuickRule
import java.util.*

/**
 * EditText with mapped {@link ParamDescription} to it
 *
 * @author apopov
 */
class ParamEditTextView(private val context : Context,
                        param : ParamDescription) : ParamView(param) {
    private val editText = createEditText(param)

    private fun createEditText(param: ParamDescription) : TextView =
        when (param.valueClass) {
            Int::class.java -> createNumericField(false)
            Long::class.java -> createNumericField(false)
            Double::class.java -> createNumericField(true)
            Float::class.java -> createNumericField(true)
            Date::class.java -> createDateField()
            else -> createSingleLineField()
        }

    private fun createNumericField(realNumber : Boolean) : EditText {
        val editText = EditText(context)
        val inputType =
                if (realNumber) InputType.TYPE_CLASS_NUMBER
                else InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        editText.inputType = inputType

        if (param.paramCategory == ParamCategory.PRICE) {
            MiscUtils.configureAsPriceField(editText)
        }

        return editText
    }

    private fun createSingleLineField() : EditText {
        val editText = EditText(context)
        editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        return editText
    }

    private fun createDateField() : EditText {
        val editText = EditText(context)
        editText.inputType = InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE
        return editText
    }

    override fun configureValidation(validator: Validator) {
        if (param.required) {
            validator.put(editText, NotEmptyQuickRule())
        }
    }

    override fun getView(): View {
        return editText
    }

    override fun setViewValue(propertyValue: Any?) {
        editText.text = propertyValue.toString()
    }

    override fun getViewValue(): Any? {
        if (param.paramCategory == ParamCategory.PRICE) {
            return editText.text.toString().substring(1)
        }
        return editText.text.toString()
    }

    override fun convertFromViewValueToPropertyClass(propertyValue: Any?): Any? {
        if (propertyValue == null || propertyValue == "") {
            return null
        }

        return MiscUtils.convertString(propertyValue as String, param.valueClass)
    }


}