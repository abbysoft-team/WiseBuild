package ru.abbysoft.wisebuild.ru.abbysoft.wisebuild.model;

/**
 * Motherboard part
 */
public class Motherboard extends ComputerPart {

    private final SocketType socketType;

    Motherboard(String name, SocketType socketType) {
        super(name);

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
