package ru.abbysoft.wisebuild.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract computer part component
 */
public abstract class ComputerPart {

    private static long nextID = 0;

    /**
     * unique part identifier
     */
    private final long id;

    private final String name;
    private volatile String description;
    private volatile Bitmap photo;
    private volatile long priceUsd;
    private final ComputerPartType type;

    /**
     * This is additional parameters of any concrete part
     *
     * Derived classes must use addParam() to specify this
     * parameters. This will be used by gui in order to display
     * some features of this part.
     */
    private final List<PartParameter> parameters;

    protected ComputerPart(String name, ComputerPartType type) {
        this.name = name;
        this.type = type;
        this.id = nextID++;

        parameters = new ArrayList<>();
        initParameters();
    }

    /**
     * This function must init part parameters.
     *
     * Part parameters used by gui in order to display
     * some key parameters that part provide.
     *
     * Each concrete ComputerPart type must specify their own
     * parameters.
     *
     * Ex. CPU part type can init following part parameters:
     * {freq, num. of cores, manufacturer etc}. This parameters will
     * be displayed in gui.
     */
    protected abstract void initParameters();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public long getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(long priceUsd) {
        this.priceUsd = priceUsd;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComputerPartType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void addParameter(String name, Object value) {
        this.parameters.add(new PartParameter(name, value));
    }

    public enum ComputerPartType {
        CPU("CPU", ru.abbysoft.wisebuild.model.CPU.class),
        MEMORY_MODULE("Memory module", MemoryModule.class),
        MOTHERBOARD("Motherboard", Motherboard.class),

        ASSEMBLED_PC("Assembled PC", AssembledPC.class);

        private final String readableName;
        private final Class objectClass;

        ComputerPartType(String name, Class objectClass) {
            this.readableName = name;
            this.objectClass = objectClass;
        }

        public static List<ComputerPartType> getEntriesWithoutAssembly() {
            List<ComputerPartType> partTypes = new ArrayList<>(values().length-1);
            for (ComputerPartType type : values()) {
                if (type != ASSEMBLED_PC) {
                    partTypes.add(type);
                }
            }
            return partTypes;
        }

        public String getReadableName() {
            return readableName;
        }

        public Class getObjectClass() {
            return objectClass;
        }
    }
}
