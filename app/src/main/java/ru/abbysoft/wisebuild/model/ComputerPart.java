package ru.abbysoft.wisebuild.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.abbysoft.wisebuild.databinding.ParamDescription;
import ru.abbysoft.wisebuild.utils.ModelUtils;

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
    private volatile int releaseYear;
    private volatile int releaseQuarter;
    private volatile Bitmap photo;
    private volatile long priceUsd;
    private final ComputerPartType type;

    protected ComputerPart(@NonNull ComputerPartType type) {
        this.type = type;
        this.id = nextID++;
    }

    public ArrayList<ParamDescription> getAllParameters() {
        ArrayList<ParamDescription> params = new ArrayList<>(10);

        params.add(ModelUtils.createStringParameter("name"));
        params.add(ModelUtils.createStringParameter("description"));
        params.add(ModelUtils.createLongParameter("priceUsd"));
        params.add(ModelUtils.createIntParameter("releaseYear"));
        params.add(ModelUtils.createIntParameter("releaseQuarter"));

        params.addAll(getTypeParams());

        return params;
    }

    public abstract ArrayList<ParamDescription> getTypeParams();

    public String getName() {
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
            return getName();
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

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getReleaseQuarter() {
        return releaseQuarter;
    }

    public void setReleaseQuarter(int releaseQuarter) {
        this.releaseQuarter = releaseQuarter;
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
