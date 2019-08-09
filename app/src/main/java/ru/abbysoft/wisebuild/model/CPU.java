package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;

import ru.abbysoft.wisebuild.databinding.ParamDescription;
import ru.abbysoft.wisebuild.utils.ModelUtils;

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
        return String.format("%s %s with %d cores", manufacturer, getName(), cores);
    }

    @Override
    public ArrayList<ParamDescription> getTypeParams() {
        ArrayList<ParamDescription> params = new ArrayList<>(3);

        params.add(ModelUtils.createStringParameter("manufacturer"));
        params.add(ModelUtils.createIntParameter("cores"));

        return params;
    }
}
