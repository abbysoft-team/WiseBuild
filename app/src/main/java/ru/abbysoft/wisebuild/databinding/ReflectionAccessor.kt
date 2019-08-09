package ru.abbysoft.wisebuild.databinding

import android.util.Log
import ru.abbysoft.wisebuild.utils.MiscUtils
import java.lang.IllegalStateException
import java.lang.reflect.Method

/**
 * Accessor which access parameter using reflection api
 *
 * Note: public getters must be specified for desired object
 *
 * @author apopov
 */
class ReflectionAccessor(private val fieldName : String) : Accessor {

    companion object {
        private const val LOG_TAG = "ReflectionAccessor"
    }

    override fun get(inputObject: Any): Any? {
        val getter =  getMethodFromAllSuperclasses(inputObject,
                MiscUtils.getGetterName(fieldName, inputObject.javaClass))
        return getter?.let { it(inputObject) }
    }

    private fun getMethodFromAllSuperclasses(inputObject: Any, name : String): Method {
        var claz: Class<in Any>? = inputObject.javaClass
        var method: Method?
        while (claz != null) {
            method = getMethodFromClass(claz, name)
            if (method != null) {
                return method
            }
            claz = claz.superclass
        }

        Log.e(LOG_TAG, "cannot access field $fieldName")
        throw IllegalStateException("Reflection accessor for field $fieldName is wrong")
    }

    private fun getMethodFromClass(claz : Class<Any>, name : String) : Method? {
        try {
            val methods = claz.declaredMethods
            for (method in methods) {
                if (method.name == name) {
                    return method
                }
            }
        } catch (e : NoSuchMethodException) {
        }

        return null
    }

    override fun set(inputObject: Any, value: Any) {
        val method = getMethodFromAllSuperclasses(inputObject, MiscUtils.getSetterName(fieldName))
        method.invoke(inputObject, value)
    }


}