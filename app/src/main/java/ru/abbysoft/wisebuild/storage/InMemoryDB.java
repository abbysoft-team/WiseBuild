package ru.abbysoft.wisebuild.storage;

import java.util.ArrayList;

import ru.abbysoft.wisebuild.model.ComputerPart;

/**
 * Storage that contains all data in memory and not persistent
 *
 * Singleton pattern
 *
 * @author apopov
 */
public enum InMemoryDB implements DBInterface {

    INSTANCE;

    private final ArrayList<ComputerPart> parts;

    InMemoryDB() {
        parts = new ArrayList<>();
    }

    @Override
    public void storePart(ComputerPart part) {
        parts.add(part);
    }

    @Override
    public ArrayList<ComputerPart> getAllComponents() {
        return parts;
    }

    @Override
    public ArrayList<ComputerPart> getComponentsOfType(ComputerPart.ComputerPartType type) {
        ArrayList<ComputerPart> result = new ArrayList<>();

        for (ComputerPart part : parts) {
            if (part.getType() == type) {
                result.add(part);
            }
        }

        return result;
    }
}
