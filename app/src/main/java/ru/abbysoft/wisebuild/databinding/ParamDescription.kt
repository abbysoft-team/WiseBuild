package ru.abbysoft.wisebuild.databinding

/**
 * Description of parameter
 *
 * Part of custom databinding system (Accessor, ParamDescription, ParamView)
 *
 * @author apopov
 */
data class ParamDescription(val name : String,
                            val valueClass : Class<out Any>,
                            val accessor : Accessor)