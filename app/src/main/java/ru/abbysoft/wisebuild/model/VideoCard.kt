package ru.abbysoft.wisebuild.model

import ru.abbysoft.wisebuild.databinding.ParamDescription
import ru.abbysoft.wisebuild.utils.ModelUtils
import java.util.ArrayList

class VideoCard : ComputerPart(ComputerPartType.VIDEO_CARD) {
    override val typeParams: ArrayList<ParamDescription> = createParameters()

    var memoryMb : Int? = null
    var memoryType : MemoryType? = null

    private fun createParameters(): ArrayList<ParamDescription> {
        val params = ArrayList<ParamDescription>(4)

        params.add(ModelUtils.createIntParameter("memoryMb"))
        params.add(ModelUtils.createParameter("memoryType", MemoryType::class.java))

        return params
    }

}

enum class MemoryType {
    GDDR3,
    GDDR4,
    GDDR5
}