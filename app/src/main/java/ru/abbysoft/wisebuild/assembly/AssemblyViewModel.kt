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

    private val cpuUnits : ArrayList<CPU> = ArrayList()
    private val memoryModules : ArrayList<MemoryModule> = ArrayList()
    private val motherboards : ArrayList<Motherboard> = ArrayList()

    var currentCpu : MutableLiveData<CPU> = MutableLiveData()
    var currentMotherboard : MutableLiveData<Motherboard> = MutableLiveData()
    var currentMemory : MutableLiveData<MemoryModule> = MutableLiveData()

    var currentParts : MutableLiveData<ArrayList<ComputerPart?>> = MutableLiveData()

    fun calculateTotalPrice() : Long {
        var price : Long = 0

        if (currentParts.value == null) {
            return 0
        }

        for (part in currentParts.value as ArrayList<ComputerPart?>) {
            if (part != null) {
                price += part.priceUsd
            }
        }

        return price
    }

    fun addButtonClicked(type : ComputerPart.ComputerPartType) {
        nextAction.value = when (type) {
            ComputerPart.ComputerPartType.CPU -> AssemblyAction.ADD_CPU
            ComputerPart.ComputerPartType.MEMORY_MODULE -> AssemblyAction.ADD_MEMORY
            ComputerPart.ComputerPartType.MOTHERBOARD -> AssemblyAction.ADD_MOTHERBOARD
            ComputerPart.ComputerPartType.ASSEMBLED_PC -> {AssemblyAction.NOTHING}
        }
    }

    fun saveAssembly() {
        nextAction.value = AssemblyAction.SAVE_ASSEMBLY
    }

    fun actionDone() {
        nextAction.value = AssemblyAction.NOTHING
    }

    fun newPartAdded(part: ComputerPart, partType: ComputerPart.ComputerPartType) {
        when (partType) {
            ComputerPart.ComputerPartType.CPU -> {
                currentCpu.value = part as CPU
                cpuUnits.add(part)
            }
            ComputerPart.ComputerPartType.MEMORY_MODULE -> {
                currentMemory.value = part as MemoryModule
                memoryModules.add(part)
            }
            ComputerPart.ComputerPartType.MOTHERBOARD -> {
                currentMotherboard.value = part as Motherboard
                motherboards.add(part)
            }
            ComputerPart.ComputerPartType.ASSEMBLED_PC -> {}
        }

        val parts = ArrayList<ComputerPart?>()
        parts.add(currentMemory.value)
        parts.add(currentCpu.value)
        parts.add(currentMotherboard.value)

        currentParts.value = parts
    }

    enum class AssemblyAction {
        NOTHING,
        ADD_CPU,
        ADD_MOTHERBOARD,
        ADD_MEMORY,
        SAVE_ASSEMBLY
    }
}