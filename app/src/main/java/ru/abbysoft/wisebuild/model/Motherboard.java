package ru.abbysoft.wisebuild.model;


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
