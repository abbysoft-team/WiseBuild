package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;

import ru.abbysoft.wisebuild.exception.SlotLimitException;

/**
 * Assembled PC is container for several
 * computer parts components such as motherboard, CPU and memory
 *
 * @author apopov
 */
public class AssembledPC extends ComputerPart {
    private Motherboard motherboard;
    private CPU cpu;
    private final ArrayList<MemoryModule> memoryModules;

    /**
     * Constructor
     *
     * @param name computer name
     */
    public AssembledPC(String name) {
        super(name, ComputerPartType.ASSEMBLED_PC);

        memoryModules = new ArrayList<>(getMaxMemoryModules());
    }

    private int getMaxMemoryModules() {
        // TODO count real number of supported memory modules
        // this will depends on motherboard being used
        return 4;
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public void addMemoryModule(MemoryModule module) throws SlotLimitException {
        if (memoryModules.size() + 1 > getMaxMemoryModules()) {
            throw new SlotLimitException("Max memory modules number exceeded");
        }

        memoryModules.add(module);
    }
}
