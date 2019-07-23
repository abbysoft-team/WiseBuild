package ru.abbysoft.wisebuild.model;

/**
 * Processor component model object
 *
 */
public class CPU extends ComputerPart {

    private final String manufacturer;
    private final int cores;

    public CPU(String name, String manufacturer, int cores) {
        super(name, ComputerPartType.CPU);

        this.manufacturer = manufacturer;
        this.cores = cores;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getCores() {
        return cores;
    }
}
