package ru.abbysoft.wisebuild.storage;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import ru.abbysoft.wisebuild.model.ComputerPart;

/**
 * Interface for DB realisation
 *
 * Store computer parts info
 */
public interface DBInterface {

    void storePart(ComputerPart part);
    ArrayList<ComputerPart> getAllComponents();

    /**
     * Get part with specific id
     *
     * @param id identifier of part being requested
     *
     * @return computer part with specified id or null
     */
    @Nullable
    ComputerPart getPart(long id);

    ArrayList<ComputerPart> getComponentsOfType(ComputerPart.ComputerPartType type);
}
