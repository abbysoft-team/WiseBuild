package ru.abbysoft.wisebuild.ru.abbysoft.wisebuild.model;

import android.media.Image;

/**
 * Abstract computer part component
 */
public abstract class ComputerPart {

    private final String name;
    private volatile String description;
    private volatile Image photo;
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

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
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
        CPU,
        MEMORY_MODULE,
        MOTHERBOARD,
    }
}
