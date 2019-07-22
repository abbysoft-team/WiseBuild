package ru.abbysoft.wisebuild.storage;

import java.util.ArrayList;

import ru.abbysoft.wisebuild.ru.abbysoft.wisebuild.model.ComputerPart;

/**
 * Interface for DB realisation
 *
 * Store computer parts info
 */
public interface DBInterface {

    void storePart(ComputerPart part);
    ArrayList<ComputerPart> getAllComponents();
    ArrayList<ComputerPart> getComponentsOfType(ComputerPart.ComputerPartType type);
}
