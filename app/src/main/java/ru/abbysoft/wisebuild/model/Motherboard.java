package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Motherboard part
 */
public class Motherboard extends ComputerPart {

    private final SocketType socketType;

    public Motherboard(String name, SocketType socketType) {
        super(name, ComputerPartType.MOTHERBOARD);

        this.socketType = socketType;
    }

    @Override
    public List<PartParameter> getParameters() {
        ArrayList<PartParameter> parameters = new ArrayList<>(1);

        parameters.add(new PartParameter("Type", socketType.name()));

        return parameters;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getName(), socketType.name());
    }


    public enum SocketType {
        LGA1155,
        LGA2011,
        AM3,
    }

    public SocketType getSocketType() {
        return socketType;
    }
}
