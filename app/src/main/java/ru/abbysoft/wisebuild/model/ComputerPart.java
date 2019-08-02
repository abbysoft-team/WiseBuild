package ru.abbysoft.wisebuild.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Abstract computer part component
 *
 * WARNING:
 * All derived part types must have constructor with 0 arguments!
 *
 * @author apopov
 */
public abstract class ComputerPart {

    private static long nextID = 0;

    /**
     * unique part identifier
     */
    private final long id;

    private volatile String name;
    private volatile String trimmedName;
    private volatile String description;
    private volatile Date releaseDate;
    private volatile Bitmap photo;
    private volatile long priceUsd;
    private final ComputerPartType type;

    protected ComputerPart(@NonNull ComputerPartType type) {
        this.type = type;
        this.id = nextID++;
    }

    /**
     * Get all part parameters, common and type specific
     *
     * @return all part parameters
     */
    public List<PartParameter> getParameters() {
        List<PartParameter> parameters = new ArrayList<>();

        parameters.add(new PartParameter("name", name));
        parameters.add(new PartParameter("trimmedName", trimmedName));
        parameters.add(new PartParameter("description", description));
        parameters.add(new PartParameter("photo", photo));
        parameters.add(new PartParameter("priceUsd", priceUsd));
        parameters.add(new PartParameter("type", type));
        parameters.add(new PartParameter("releaseDate", releaseDate));

        parameters.addAll(getTypeParameters());

        return parameters;
    }

    /**
     * Return additional parameters of any concrete part
     *
     * Derived classes must use addParam() to specify this
     * parameters. This will be used by gui in order to display
     * some features of this part.
     * @return parameters of part
     */
    public abstract List<PartParameter> getTypeParameters();

    public String getFullName() {
        return name;
    }

    public String getTrimmedName() {
        return trimmedName;
    }

    public void setName(String name) {
        this.name = name;
        this.trimmedName = createTrimmedName();
    }

    private String createTrimmedName() {
        int MAX_TRIMMED_NAME_LENGTH = 20;
        if (name.length() <= MAX_TRIMMED_NAME_LENGTH) {
            return getFullName();
        }


        String trimmed = name.substring(0, MAX_TRIMMED_NAME_LENGTH);
        trimmed += "...";

        return trimmed;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
