package ru.abbysoft.wisebuild.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collection;
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

    protected ComputerPart(String name, ComputerPartType type) {
        this.name = name;
        this.type = type;
        this.id = nextID++;
    }

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
