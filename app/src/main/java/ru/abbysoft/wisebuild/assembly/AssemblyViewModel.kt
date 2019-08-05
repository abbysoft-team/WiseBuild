package ru.abbysoft.wisebuild.assembly

import androidx.lifecycle.ViewModel
import ru.abbysoft.wisebuild.model.CPU
import ru.abbysoft.wisebuild.model.ComputerPart
import ru.abbysoft.wisebuild.model.MemoryModule
import ru.abbysoft.wisebuild.model.Motherboard

/**
 * All model data and data manipulations of #CreateAssemblyActivity
 *
 * @author apopov
 */
class AssemblyViewModel : ViewModel() {

    val cpuUnits : ArrayList<CPU> = ArrayList()
    val memoryModules : ArrayList<MemoryModule> = ArrayList()
    val motherboards : ArrayList<MemoryModule> = ArrayList()

    var currentCpu : CPU? = null
    var currentMotherboard : Motherboard? = null
    var currentMemory : MemoryModule? = null

    fun addPart(type : ComputerPart.ComputerPartType) {

    }

    fun partCount(type : ComputerPart.ComputerPartType) : Int = when(type) {
        ComputerPart.ComputerPartType.CPU -> cpuUnits.size
        ComputerPart.ComputerPartType.MEMORY_MODULE -> memoryModules.size
        ComputerPart.ComputerPartType.MOTHERBOARD -> motherboards.size
        ComputerPart.ComputerPartType.ASSEMBLED_PC -> -1 // unwanted branch
    }
}