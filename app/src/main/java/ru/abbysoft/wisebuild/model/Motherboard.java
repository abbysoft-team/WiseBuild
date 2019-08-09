package ru.abbysoft.wisebuild.model;


import java.util.ArrayList;

import ru.abbysoft.wisebuild.databinding.ParamDescription;
import ru.abbysoft.wisebuild.utils.ModelUtils;

/**
 * Motherboard part
 */
public class Motherboard extends ComputerPart {

    private SocketType socketType;

    public Motherboard() {
        super(ComputerPartType.MOTHERBOARD);
    }

    @Override
    public String toString() {
        return String.format("%s %s", getName(),
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

    @Override
    public ArrayList<ParamDescription> getTypeParams() {
        ArrayList<ParamDescription> params = new ArrayList<>(3);

        params.add(ModelUtils.createParameter("socketType", SocketType.class));

        return params;
    }
}
