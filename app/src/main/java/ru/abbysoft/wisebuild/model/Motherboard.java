package ru.abbysoft.wisebuild.model;

import java.util.ArrayList;
import java.util.Collection;
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

        parameters.add(new PartParameter("type", socketType.name()));

        return parameters;
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
