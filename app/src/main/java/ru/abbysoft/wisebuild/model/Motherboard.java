package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Motherboard part
 */
public class Motherboard extends ComputerPart {

    private SocketType socketType;

    public Motherboard() {
        super(ComputerPartType.MOTHERBOARD);
    }

    @Override
    public List<PartParameter> getTypeParameters() {
        ArrayList<PartParameter> parameters = new ArrayList<>(1);

        parameters.add(new PartParameter("Type",
                socketType != null ? socketType.name() : ""));

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
