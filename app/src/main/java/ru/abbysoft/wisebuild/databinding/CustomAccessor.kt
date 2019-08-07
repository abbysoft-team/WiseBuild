package ru.abbysoft.wisebuild.databinding

/**
 * Custom accessor
 *
 * User of this class specifies getter and setter explicitly
 *
 * @author apopov
 */
class CustomAccessor(private val getter : (Any) -> Any,
                     private val setter : (Any, Any) -> Void) : Accessor {

    override fun get(inputObject : Any) : Any {
        return getter(inputObject)
    }

    override fun set(inputObject : Any, value : Any) {
        setter(inputObject, value)
    }
}