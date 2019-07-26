package ru.abbysoft.wisebuild.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Abstract computer part component
 */
public abstract class ComputerPart {

    private final String name;
    private volatile String description;
    private volatile Bitmap photo;
    private volatile long priceUsd;
    private final ComputerPartType type;

    protected ComputerPart(String name, ComputerPartType type) {
        this.name = name;
        this.type = type;
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

    public enum ComputerPartType {
        CPU("CPU"),
        MEMORY_MODULE("Memory module"),
        MOTHERBOARD("Motherboard"),

        ASSEMBLED_PC("Assembled PC");

        private final String readableName;

        ComputerPartType(String name) {
            this.readableName = name;
        }

        public static Collection<ComputerPartType> getEntriesWithoutAssembly() {
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
    }
}
