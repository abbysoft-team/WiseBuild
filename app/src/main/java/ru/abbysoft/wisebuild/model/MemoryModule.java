package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.List;

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
        super(name, ComputerPartType.MEMORY_MODULE);

        this.type = type;
        this.capacityMb = capacityMb;
    }

    public MemoryType getMemoryType() {
        return type;
    }

    public int getCapacityMb() {
        return capacityMb;
    }

    @Override
    public List<PartParameter> getTypeParameters() {
        ArrayList<PartParameter> parameters = new ArrayList<>(2);

        parameters.add(new PartParameter("Type", type.getName()));
        parameters.add(new PartParameter("Capacity (mb)", capacityMb));

        return parameters;
    }

    @Override
    public String toString() {
        return String.format("%s with %d mb of %s memory", getFullName(), getCapacityMb(), type.name);
    }

    /**
     * Memory type
     */
    public enum MemoryType {
        DDR("DDR"),
        DDR2("DDR2"),
        DDR3("DDR3"),
        DDR4("DDR4");

        private String name;

        MemoryType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
