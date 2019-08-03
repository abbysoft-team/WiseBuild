package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MemoryModule part
 *
 */
public class MemoryModule extends ComputerPart {

    private MemoryType type;
    private int capacityMb;

    /**
     * Default constructor
     */
    public MemoryModule() {
        super(ComputerPartType.MEMORY_MODULE);
    }

    public MemoryType getMemoryType() {
        return type;
    }

    public void setMemoryType(MemoryType type) {
        this.type = type;
    }

    public int getCapacityMb() {
        return capacityMb;
    }

    public void setCapacityMb(int capacityMb) {
        this.capacityMb = capacityMb;
    }

    @Override
    public String toString() {
        return String.format("%s with %d mb of %s memory", getFullName(), getCapacityMb(),
                type != null ? type.name : "");
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
