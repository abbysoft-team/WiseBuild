package ru.abbysoft.wisebuild.model;

/**
 * MemoryModule part
 *
 */
public class MemoryModule extends ComputerPart {

    private final MemoryType type;
    private final int capacityMb;

    /**
     * Default constructor
     *
     * @param name component name
     * @param type type of memory
     * @param capacityMb capacity in MB
     */
    public MemoryModule(String name, MemoryType type, int capacityMb) {
        super(name);

        this.type = type;
        this.capacityMb = capacityMb;
    }

    public MemoryType getType() {
        return type;
    }

    public int getCapacityMb() {
        return capacityMb;
    }

    public enum MemoryType {
        DDR,
        DDR2,
        DDR3,
        DDR4
    }
}
