package ru.abbysoft.wisebuild.databinding

import android.content.Context
import android.view.View
import java.util.*

/**
 * View that have underlying mapped parameter {@link ParamDescription}
 *
 * Use #getView() to receive formatted View that represents parameter
 * Use #setInput() to set field value
 * Use #setProperty() to get info from view into specified object
 *
 * @author apopov
 */
abstract class ParamView (val param : ParamDescription) {

    /**
     * Return underlying android view
     *
     * @return android compatible view
     */
    abstract fun getView() : View

    /**
     * Set object from which to get parameter data
     *
     * @param inputObject object from which to get parameter data
     */
    fun setInput(inputObject : Any) {
        val propertyValue = param.accessor.get(inputObject)
        setViewValue(propertyValue)
    }

    /**
     * Set view value from property value
     */
    protected abstract fun setViewValue(propertyValue : Any?)

    /**
     * Get info from this view and set corresponding parameter
     * value into specified object
     *
     * @param inputObject object containing updated parameter
     */
    fun setProperty(inputObject: Any) {
        val propertyValue = getViewValue()
        param.accessor.set(inputObject, propertyValue)
    }

    /**
     * Get value of view
     */
    protected abstract fun getViewValue() : Any?
    
    companion object {
        /**
         * Create ParamView for specified parameter
         *
         * @param context context
         * @param param parameter
         * @return null if valueClass of param does not supported
         */
        fun createFor(context: Context, param: ParamDescription): ParamView? {
            val claz = param.valueClass

            if (String::class.java.isAssignableFrom(claz)) {
                return ParamEditTextView(context, param)
            } else if (claz == Long::class.java || claz == Int::class.java
                    || claz == Double::class.java || claz == Float::class.java) {
                return ParamEditTextView(context, param)
            } else if (Enum::class.java.isAssignableFrom(claz)) {
                return SpinnerParamView(context, param)
            } else if (Date::class.java.isAssignableFrom(claz)) {
                return ParamEditTextView(context, param)
            }

            return null
        }
    }
}