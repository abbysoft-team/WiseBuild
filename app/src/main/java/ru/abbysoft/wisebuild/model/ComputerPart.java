package ru.abbysoft.wisebuild.model;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Abstract computer part component
 */
public abstract class ComputerPart {

    private final String name;
    private volatile String description;
    private volatile Bitmap photo;
    private volatile long priceUsd;

    protected ComputerPart(String name) {
        this.name = name;
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

    public enum ComputerPartType {
        CPU("CPU"),
        MEMORY_MODULE("Memory module"),
        MOTHERBOARD("Motherboard"),
        ;

        private final String readableName;

        ComputerPartType(String name) {
            this.readableName = name;
        }

        public String getReadableName() {
            return readableName;
        }
    }
}
