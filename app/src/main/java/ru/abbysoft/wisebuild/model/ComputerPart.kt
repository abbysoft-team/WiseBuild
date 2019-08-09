package ru.abbysoft.wisebuild.model

import android.graphics.Bitmap
import ru.abbysoft.wisebuild.databinding.ParamCategory

import java.util.ArrayList

import ru.abbysoft.wisebuild.databinding.ParamDescription
import ru.abbysoft.wisebuild.utils.ModelUtils

/**
 * Abstract computer part component
 *
 * WARNING:
 * All derived part types must have constructor with 0 arguments!
 *
 * @author apopov
 */
abstract class ComputerPart protected constructor(val type: ComputerPartType) {

    /**
     * unique part identifier
     */
    val id: Long
    var name: String? = null
        set(name) {
            field = name
            this.trimmedName = createTrimmedName()
        }
    var trimmedName: String? = null
        private set
    var description: String? = null
    var releaseYear: Int = 0
    var releaseQuarter: Int = 0
    var photo: Bitmap? = null
    var priceUsd: Long = 0

    val allParameters: ArrayList<ParamDescription>
        get() {
            val params = ArrayList<ParamDescription>(10)

            params.add(ModelUtils.createStringParameter("name").apply { required = true })
            params.add(ModelUtils.createStringParameter("description"))
            params.add(ModelUtils.createLongParameter("priceUsd")
                    .apply {
                        paramCategory = ParamCategory.PRICE
                        required = true })
            params.add(ModelUtils.createIntParameter("releaseYear"))
            params.add(ModelUtils.createIntParameter("releaseQuarter"))

            params.addAll(typeParams)

            return params
        }

    abstract val typeParams: ArrayList<ParamDescription>

    init {
        this.id = nextID++

        this.releaseQuarter = -1
        this.releaseYear = -1
    }

    private fun createTrimmedName(): String? {
        val MAX_TRIMMED_NAME_LENGTH = 20
        if (this.name!!.length <= MAX_TRIMMED_NAME_LENGTH) {
            return name
        }


        var trimmed = this.name!!.substring(0, MAX_TRIMMED_NAME_LENGTH)
        trimmed += "..."

        return trimmed
    }

    enum class ComputerPartType constructor(val readableName: String, val objectClass: Class<*>) {
        CPU("CPU", ru.abbysoft.wisebuild.model.CPU::class.java),
        MEMORY_MODULE("Memory module", MemoryModule::class.java),
        MOTHERBOARD("Motherboard", Motherboard::class.java),
        ASSEMBLED_PC("Assembled PC", AssembledPC::class.java),
        VIDEO_CARD("Video card", VideoCard::class.java);


        companion object {
            val entriesWithoutAssembly: List<ComputerPartType>
                get() {
                    val partTypes = ArrayList<ComputerPartType>(values().size - 1)
                    for (type in values()) {
                        if (type != ASSEMBLED_PC) {
                            partTypes.add(type)
                        }
                    }
                    return partTypes
                }
        }
    }

    companion object {

        private var nextID: Long = 0
    }
}
