package ru.abbysoft.wisebuild.model;

/**
 * Some parameter of a part
 *
 * @author apopov
 */
public class PartParameter {

    private final String name;
    private final Object value;

    public PartParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    /**
     * Get class of value
     * @return class
     */
    public Class getValueClass() {
        return value.getClass();
    }
}
