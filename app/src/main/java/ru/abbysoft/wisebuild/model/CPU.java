package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.List;

import ru.abbysoft.wisebuild.databinding.FieldMapping;

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
    public List<FieldMapping> getTypeParameters() {
        ArrayList<FieldMapping> parameters = new ArrayList<>(2);

        parameters.add(new FieldMapping("Manufacturer", manufacturer, String.class));
        parameters.add(new FieldMapping("Cores", cores, int.class));

        return parameters;
    }

    @Override
    public String toString() {
        return String.format("%s %s with %d cores", manufacturer, getFullName(), cores);
    }
}
