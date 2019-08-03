package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.List;

import ru.abbysoft.wisebuild.databinding.FieldMapping;

/**
 * Motherboard part
 */
public class Motherboard extends ComputerPart {

    private SocketType socketType;

    public Motherboard() {
        super(ComputerPartType.MOTHERBOARD);
    }

    @Override
    public List<FieldMapping> getTypeParameters() {
        ArrayList<FieldMapping> parameters = new ArrayList<>(1);

        parameters.add(new FieldMapping("Type",
                socketType != null ? socketType.name() : "", SocketType.class));

        return parameters;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getFullName(),
                socketType != null ? socketType.name() : "");
    }


    public enum SocketType {
        LGA1155,
        LGA2011,
        AM3,
    }

    public SocketType getSocketType() {
        return socketType;
    }

    public void setSocketType(SocketType type) {
        socketType = type;
    }
}
