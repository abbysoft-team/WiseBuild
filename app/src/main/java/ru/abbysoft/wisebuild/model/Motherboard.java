package ru.abbysoft.wisebuild.model;

/**
 * Motherboard part
 */
public class Motherboard extends ComputerPart {

    private final SocketType socketType;

    public Motherboard(String name, SocketType socketType) {
        super(name, ComputerPartType.MOTHERBOARD);

        this.socketType = socketType;
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
