package ru.abbysoft.wisebuild.storage;

import androidx.annotation.Nullable;

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

    @Nullable
    @Override
    public ComputerPart getPart(long id) {
        for (ComputerPart part : parts) {
            if (part.getId() == id) {
                return part;
            }
        }

        return null;
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
