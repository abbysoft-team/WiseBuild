package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<PartParameter> getParameters() {
        ArrayList<PartParameter> parameters = new ArrayList<>(2);

        parameters.add(new PartParameter("manufacturer", manufacturer));
        parameters.add(new PartParameter("cores", cores));

        return parameters;
    }
}
