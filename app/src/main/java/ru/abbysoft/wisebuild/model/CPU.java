package ru.abbysoft.wisebuild.model;

/**
 * Processor component model object
 *
 */
public class CPU extends ComputerPart {

    private String manufacturer;
    private int cores;

    public CPU() {
        super(ComputerPartType.CPU);

    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getCores() {
        return cores;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    @Override
    public String toString() {
        return String.format("%s %s with %d cores", manufacturer, getFullName(), cores);
    }
}
