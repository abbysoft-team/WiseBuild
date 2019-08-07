package ru.abbysoft.wisebuild.databinding

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import ru.abbysoft.wisebuild.R
import ru.abbysoft.wisebuild.utils.MiscUtils.getEnumValues


/**
 * Spinner view with mapped parameter to it
 *
 * @author apopov
 */
class SpinnerParamView(private val context : Context, param : ParamDescription) : ParamView(param) {

    private val spinnerAdapter : ArrayAdapter<Any> = createSpinnerAdapter()
    private val spinner = createSpinner(spinnerAdapter)

    private fun createSpinnerAdapter(): ArrayAdapter<Any> {
        return ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, getEnumValues(param.valueClass))
    }

    private fun createSpinner(adapter : SpinnerAdapter) : Spinner {
        val spinner = Spinner(context)
        spinner.adapter = adapter
        return spinner
    }

    override fun getView(): View {
        return spinner
    }

    override fun setViewValue(propertyValue: Any?) {
        val index = spinnerAdapter.getPosition(propertyValue)
        if (index == -1) {
            return
        }

        spinner.setSelection(index)
    }

    override fun getViewValue(): Any? {
        return spinner.selectedItem
    }

}