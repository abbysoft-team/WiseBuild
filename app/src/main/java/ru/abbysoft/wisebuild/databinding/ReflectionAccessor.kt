package ru.abbysoft.wisebuild.databinding

/**
 * Accessor which access parameter using reflection api
 *
 * Note: public getters must be specified for desired object
 *
 * @author apopov
 */
class ReflectionAccessor(private val fieldName : String) : Accessor {

    override fun get(inputObject: Any): Any? {
        return inputObject.javaClass.getField(fieldName).get(inputObject)
    }

    override fun set(inputObject: Any, value: Any?) {
        inputObject.javaClass.getField(fieldName).set(inputObject, value)
    }


}