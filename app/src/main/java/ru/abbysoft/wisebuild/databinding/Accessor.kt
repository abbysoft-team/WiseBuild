package ru.abbysoft.wisebuild.databinding

/**
 * Interface for parameter accessor
 *
 * Object implementing this interface should tell caller how to access some property from
 * valid object
 *
 * @author apopov
 */
interface Accessor {

    /**
     * Get value of object
     *
     * @param inputObject object from which to get property
     * @return parameter of input object which this accessor describes
     */
    fun get(inputObject : Any) : Any?

    /**
     * Set property of specified object
     *
     * @param inputObject object containing specified param
     * @param value value of parameter
     */
    fun set(inputObject : Any, value : Any)
}