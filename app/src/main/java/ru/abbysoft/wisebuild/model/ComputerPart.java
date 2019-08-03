package ru.abbysoft.wisebuild.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.abbysoft.wisebuild.databinding.FieldMapping;

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
    private volatile int releaseQuartal;
    private volatile Bitmap photo;
    private volatile float priceUsd;
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
    public List<FieldMapping> getParameters() {
        List<FieldMapping> parameters = new ArrayList<>();

        parameters.add(new FieldMapping("name", name, String.class));
        parameters.add(new FieldMapping("description", description, String.class));

        parameters.add(new FieldMapping("priceUsd", priceUsd, float.class)
                .setFieldType(FieldMapping.FieldType.PRICE));

        parameters.add(new FieldMapping("releaseYear", releaseYear, int.class));
        parameters.add(new FieldMapping("releaseQuartal", releaseQuartal, int.class));

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
    public abstract List<FieldMapping> getTypeParameters();

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

    public float getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(float priceUsd) {
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

    public void getReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getReleaseQuartal() {
        return releaseQuartal;
    }

    public void setReleaseQuartal(int releaseQuartal) {
        this.releaseQuartal = releaseQuartal;
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
