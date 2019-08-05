package ru.abbysoft.wisebuild.assembly

import androidx.lifecycle.MutableLiveData
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

    val nextAction : MutableLiveData<AssemblyAction> = MutableLiveData()

    val cpuUnits : ArrayList<CPU> = ArrayList()
    val memoryModules : ArrayList<MemoryModule> = ArrayList()
    val motherboards : ArrayList<Motherboard> = ArrayList()

    var currentCpu : CPU? = null
    var currentMotherboard : Motherboard? = null
    var currentMemory : MemoryModule? = null

    fun addButtonClicked(type : ComputerPart.ComputerPartType) {
        nextAction.value = when (type) {
            ComputerPart.ComputerPartType.CPU -> AssemblyAction.ADD_CPU
            ComputerPart.ComputerPartType.MEMORY_MODULE -> AssemblyAction.ADD_MEMORY
            ComputerPart.ComputerPartType.MOTHERBOARD -> AssemblyAction.ADD_MOTHERBOARD
            ComputerPart.ComputerPartType.ASSEMBLED_PC -> {AssemblyAction.NOTHING}
        }
    }

    fun saveAssemblyClicked() {
        nextAction.value = AssemblyAction.SAVE_ASSEMBLY
    }

    fun actionDone() {
        nextAction.value = AssemblyAction.NOTHING
    }

    fun newPartAdded(part: ComputerPart, partType: ComputerPart.ComputerPartType) {
        when (partType) {
            ComputerPart.ComputerPartType.CPU -> {
                currentCpu = part as CPU
                cpuUnits.add(currentCpu as CPU)
            }
            ComputerPart.ComputerPartType.MEMORY_MODULE -> {
                currentMemory = part as MemoryModule
                memoryModules.add(part)
            }
            ComputerPart.ComputerPartType.MOTHERBOARD -> {
                currentMotherboard = part as Motherboard
                motherboards.add(currentMotherboard as Motherboard)
            }
            ComputerPart.ComputerPartType.ASSEMBLED_PC -> {}
        }
    }

    enum class AssemblyAction {
        NOTHING,
        ADD_CPU,
        ADD_MOTHERBOARD,
        ADD_MEMORY,
        SAVE_ASSEMBLY
    }
}