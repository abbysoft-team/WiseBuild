package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;

import ru.abbysoft.wisebuild.databinding.ParamDescription;
import ru.abbysoft.wisebuild.utils.ModelUtils;

/**
 * MemoryModule part
 *
 */
public class MemoryModule extends ComputerPart {

    private MemoryType memoryType;
    private int capacityMb;

    /**
     * Default constructor
     */
    public MemoryModule() {
        super(ComputerPartType.MEMORY_MODULE);
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(MemoryType type) {
        this.memoryType = type;
    }

    public int getCapacityMb() {
        return capacityMb;
    }

    public void setCapacityMb(int capacityMb) {
        this.capacityMb = capacityMb;
    }

    @Override
    public String toString() {
        return String.format("%s with %d mb of %s memory", getName(), getCapacityMb(),
                memoryType != null ? memoryType.name : "");
    }

    @Override
    public ArrayList<ParamDescription> getTypeParams() {
        ArrayList<ParamDescription> params = new ArrayList<>(3);

        params.add(ModelUtils.createParameter("memoryType", MemoryType.class));
        params.add(ModelUtils.createIntParameter("capacityMb"));

        return params;
    }

    /**
     * Memory memoryType
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
